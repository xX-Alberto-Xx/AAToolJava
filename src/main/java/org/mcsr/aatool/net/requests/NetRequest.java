package org.mcsr.aatool.net.requests;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.utilities.Timer;

public abstract class NetRequest {
  public static final String INCOMING;
  public static final String OUTGOING;

  protected static final HttpClient CLIENT;

  private static final Queue<NetRequest> PENDING;
  private static final List<NetRequest> TIMED_OUT;
  private static final Set<String> ABANDONED;
  private static final Set<String> COMPLETED;
  private static final Set<String> ACTIVE;
  private static final Set<String> SUBMITTED;
  private static final Timer REQUEST_DELAY;

  protected String url;

  private int failures;
  private Stopwatch stopwatch;
  private final Timer cooldown;

  public NetRequest(String url) {}

  public static int getCompletedCount() {}
  public static int getTimedOutCount() {}
  public static int getAbandonedCount() {}
  public static int getPendingCount() {}
  public static int getActiveCount() {}
  public static int getSubmittedCount() {}

  public static void clearHistory() {}

  private static void enqueue(NetRequest request) {}

  public static void update(Time time) {}

  private static void updateTimeouts(Time time) {}

  private static void updatePending() {}

  private boolean isOnCooldown() {}

  protected final String getResponseTime() {}

  public final void enqueueOnce() {}

  public abstract CompletableFuture<Boolean> downloadAsync();

  private void complete() {}

  protected final void beginTiming() {}

  protected final void endTiming() {}

  private void updateCooldown(Time time) {}

  public final CompletableFuture<Void> sendAsync() {}

  private void fail() {}
}
