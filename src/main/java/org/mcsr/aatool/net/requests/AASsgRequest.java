package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Debug;
import org.mcsr.aatool.Paths;
import org.mcsr.aatool.data.speedrunning.Leaderboard;
import org.mcsr.aatool.utilities.HttpUtils;

public final class AASsgRequest extends NetRequest {
  public static final String SET_SEED = "SSG";

  public AASsgRequest() { super(Paths.Web.AA_SSG_RECORD); }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    Debug.log(Debug.REQUEST_SECTION, "Requested AA SSG (1.16) WR from speedrun.com");
    this.beginTiming();

    return HttpUtils.getStringAsync(this.url).thenApply(response -> {
      this.endTiming();
      return this.handleResponse(response);
    }).exceptionallyCompose(e -> {
      if (e instanceof HttpTimeoutException) {
        Debug.log(Debug.REQUEST_SECTION, "-- AA SSG (1.16) WR request timed out");
        // Request timed out, nothing left to do here
      } else if (e instanceof IOException) {
        Debug.log(Debug.REQUEST_SECTION, "-- AA SSG (1.16) WR request failed: " + e.getMessage());
        // Error getting response, safely move on
      } else {
        return CompletableFuture.failedStage(e);
      }

      this.endTiming();
      return CompletableFuture.completedStage(false);
    });
  }

  private boolean handleResponse(String response) {
    if (response == null) return false;

    response = response.strip();
    if (response.isEmpty()) return false;

    if (!Leaderboard.syncSpeedrunDotComRecord(response, false, true)) {
      Debug.log(Debug.REQUEST_SECTION, "-- Received invalid AA SSG (1.16) WR data");
      return false;
    }

    Leaderboard.saveSpeedrunDotComRecordToCache(response, false, true);
    Debug.log(Debug.REQUEST_SECTION, INCOMING + " Received AA SSG (1.16) WR from speedrun.com");
    return true;
  }
}
