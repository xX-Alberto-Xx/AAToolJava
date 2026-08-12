package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Debug;
import org.mcsr.aatool.Paths;
import org.mcsr.aatool.data.speedrunning.Leaderboard;
import org.mcsr.aatool.utilities.HttpUtils;

public final class AnyPercentRecordRequest extends NetRequest {
  public static final String RANDOM_SEED = "RSG";
  public static final String SET_SEED = "SSG";

  private final String subCategory;

  public AnyPercentRecordRequest(boolean rsg) {
    super(Paths.Web.getAnyPercentRecordUrl(rsg));
    this.subCategory = rsg ? RANDOM_SEED : SET_SEED;
  }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    Debug.log(Debug.REQUEST_SECTION, "Requested Any% " + this.subCategory + " (1.16) WR from speedrun.com");
    this.beginTiming();

    return HttpUtils.getStringAsync(this.url).thenApply(response -> {
      this.endTiming();
      return this.handleResponse(response);
    }).exceptionallyCompose(e -> {
      if (e instanceof HttpTimeoutException) {
        Debug.log(Debug.REQUEST_SECTION, "-- Any% " + this.subCategory + " (1.16) WR request timed out");
        // Request timed out, nothing left to do here
      } else if (e instanceof IOException) {
        Debug.log(
          Debug.REQUEST_SECTION,
          "-- Any% " + this.subCategory + " (1.16) WR request failed: " + e.getMessage()
        );
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

    boolean rsg = this.subCategory.equals(RANDOM_SEED);

    if (!Leaderboard.syncSpeedrunDotComRecord(response, rsg, false)) {
      Debug.log(Debug.REQUEST_SECTION, "-- Received invalid Any% " + this.subCategory + " (1.16) WR data");
      return false;
    }

    Leaderboard.saveSpeedrunDotComRecordToCache(response, rsg, false);
    Debug.log(
      Debug.REQUEST_SECTION,
      INCOMING + " Received Any% " + this.subCategory + " (1.16) WR from speedrun.com"
    );
    return true;
  }
}
