package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.net.Protocol;
import org.mcsr.aatool.utilities.Stopwatch;
import org.mcsr.aatool.utilities.Timer;

public abstract class NetRequest {
  public static final String INCOMING = "->";
  public static final String OUTGOING = "<-";

  private static final Queue<NetRequest> PENDING = new ArrayDeque<>();
  private static final List<NetRequest> TIMED_OUT = new ArrayList<>();
  private static final Set<String> ABANDONED = new HashSet<>();
  private static final Set<String> COMPLETED = new HashSet<>();
  private static final Set<String> SUBMITTED = new HashSet<>();
  private static final Timer REQUEST_DELAY = new Timer(Protocol.Requests.UPDATE_RATE);

  protected String url;

  private int failures;
  private Stopwatch stopwatch;
  private final Timer cooldown = new Timer();

  public NetRequest(String url) { this.url = url; }

  public static int getCompletedCount() { return COMPLETED.size(); }
  public static int getTimedOutCount() { return TIMED_OUT.size(); }
  public static int getAbandonedCount() { return ABANDONED.size(); }
  public static int getPendingCount() { return PENDING.size(); }
  public static int getSubmittedCount() { return SUBMITTED.size(); }

  public static void clearHistory() {
    SUBMITTED.clear();
    COMPLETED.clear();
  }

  private static void enqueue(NetRequest request) {
    // Add request to pending queue if unique
    if (!SUBMITTED.contains(request.url)) {
      PENDING.add(request);
      SUBMITTED.add(request.url);
    }
  }

  public static void update(Time time) {
    updateTimeouts(time);
    REQUEST_DELAY.update(time);

    if (REQUEST_DELAY.isExpired()) {
      REQUEST_DELAY.reset();
      updatePending();
    }
  }

  private static void updateTimeouts(Time time) {
    for (int i = TIMED_OUT.size() - 1; i >= 0; i--) {
      NetRequest request = TIMED_OUT.get(i);
      request.updateCooldown(time);

      if (!request.isOnCooldown()) {
        // Move back into pending queue
        TIMED_OUT.remove(i);
        PENDING.add(request);
      }
    }
  }

  private static void updatePending() {
    for (int i = 0; i < Protocol.Requests.MAX_CONCURRENT; i++) {
      // Start next request
      NetRequest next = PENDING.poll();
      if (next == null) return;

      next.sendAsync();
    }
  }

  private boolean isOnCooldown() { return this.cooldown.isRunning(); }

  protected final String getResponseTime() {
    return (this.stopwatch != null ? this.stopwatch.getElapsedMilliseconds() : 0) + " ms";
  }

  public final void enqueueOnce() { enqueue(this); }

  public abstract CompletableFuture<Boolean> downloadAsync();

  private void complete() { COMPLETED.add(this.url); }

  protected final void beginTiming() {
    this.stopwatch = new Stopwatch();
    this.stopwatch.start();
  }

  protected final void endTiming() { this.stopwatch.stop(); }

  private void updateCooldown(Time time) {
    if (this.cooldown.isRunning()) this.cooldown.update(time);
  }

  public final void sendAsync() {
    this.downloadAsync().thenAccept(success -> {
      if (success) this.complete();
      else this.fail();
    }).exceptionallyCompose(e -> {
      // Safely ignore and move on if network error occurred
      if (e instanceof IOException) {
        this.fail();
        return CompletableFuture.completedStage(null);
      }

      return CompletableFuture.failedStage(e);
    });
  }

  private void fail() {
    // Request failed
    this.failures++;

    if (this.failures < Protocol.Requests.MAX_RETRIES) {
      // Set cooldown to try again later
      TIMED_OUT.add(this);
      this.cooldown.setAndStart(Protocol.Requests.RETRY_COOLDOWN);
    } else {
      // Request has failed too many times. Stop trying
      ABANDONED.add(this.url);
    }
  }
}
