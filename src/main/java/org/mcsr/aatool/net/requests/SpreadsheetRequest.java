package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Debug;
import org.mcsr.aatool.Paths;
import org.mcsr.aatool.data.Credits;
import org.mcsr.aatool.data.speedrunning.Leaderboard;
import org.mcsr.aatool.utilities.HttpUtils;
import org.mcsr.aatool.utilities.Pair;

public final class SpreadsheetRequest extends NetRequest {
  private static int downloads;

  public static Set<Pair<String, String>> downloadedPages = new HashSet<>();

  private final String sheet;
  private final String page;
  private final String name;

  public SpreadsheetRequest(String name, String sheet) { this(name, sheet, "0"); }
  public SpreadsheetRequest(String name, String sheet, String page) {
    super(Paths.Web.getSpreadsheetUrl(sheet, page));
    this.name = name;
    this.sheet = sheet;
    this.page = page;
  }

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    // Logging
    Debug.log(Debug.REQUEST_SECTION, OUTGOING + " Requested spreadsheet for " + this.name);
    this.beginTiming();
    downloads++;

    // Download leaderboard spreadsheet as CSV string
    return HttpUtils.getStringAsync(this.url).thenApply(response -> {
      this.endTiming();

      if (this.handleResponse(response)) {
        downloadedPages.add(new Pair<>(this.sheet, this.page));
        return true;
      }

      return false;
    }).exceptionallyCompose(e -> {
      if (e instanceof HttpTimeoutException) {
        // Request timed out, nothing left to do here
        Debug.log(Debug.REQUEST_SECTION, "-- Spreadsheet request timed out for " + this.sheet + ':' + this.page);
      } else if (e instanceof IOException) {
        // Error getting response, safely move on
        Debug.log(
          Debug.REQUEST_SECTION,
          "-- Spreadsheet request failed for " + this.sheet + ':' + this.page + ": " + e.getMessage()
        );
      } else {
        return CompletableFuture.failedStage(e);
      }

      this.endTiming();
      return CompletableFuture.completedStage(false);
    });
  }

  private boolean handleResponse(String csv) {
    switch (this.sheet) {
      case Paths.Web.SUPPORTER_SHEET -> {
        Debug.log(Debug.REQUEST_SECTION, INCOMING + " Received supporter spreadsheet in " + this.getResponseTime());
        return Credits.syncSheet(csv);
      }

      case Paths.Web.NICKNAME_SHEET -> {
        Debug.log(Debug.REQUEST_SECTION, INCOMING + " Received nickname spreadsheet in " + this.getResponseTime());
        return Leaderboard.syncNicknames(csv);
      }
    }

    switch (this.page) {
      case Paths.Web.PRIMARY_AA_HISTORY -> {
        Debug.log(Debug.REQUEST_SECTION, INCOMING + " Received submission history spreadsheet in " + this.getResponseTime());
        return Leaderboard.syncHistory(csv, true);
      }

      case Paths.Web.AB_PAGE_CHALLENGES -> {
        Debug.log(Debug.REQUEST_SECTION, INCOMING + " Received spreadsheet " + this.name + " in " + this.getResponseTime());
        return Leaderboard.syncChallengeLeaderboards(csv);
      }
    }

    Debug.log(Debug.REQUEST_SECTION, INCOMING + " Received spreadsheet " + this.name + " in " + this.getResponseTime());
    return Leaderboard.syncSheetLeaderboard(this.sheet, this.page, csv);
  }
}
