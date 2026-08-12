package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Debug;
import org.mcsr.aatool.data.speedrunning.Leaderboard;
import org.mcsr.aatool.utilities.HttpUtils;
import org.mcsr.aatool.utilities.Pair;

public final class SrcLeaderboardRequest extends NetRequest {
  private static final String API = "https://www.speedrun.com/api/v1/leaderboards/mc";

  private static final String ANY_PERCENT_CATEGORY_VAR = "mkeyl926";
  private static final String ANY_PERCENT_VERSION_VAR = "wl33kewl";
  private static final String ANY_PERCENT_SEED_TYPE_VAR = "r8rg67rn";

  private static final String ALL_ADVANCEMENTS_CATEGORY_VAR = "xk9gz16d";
  private static final String AADV_VERSION_VAR = "789je4qn";
  private static final String AADV_SEED_TYPE_VAR = "p853vv0n";

  private static final String ALL_ACHIEVEMENTS_CATEGORY_VAR = "wk63eek1";
  private static final String AACH_VERSION_VAR = "0nw2y7xn";
  private static final String AACH_SEED_TYPE_VAR = "38do09zl";

  public static final Map<String, String> AADV_VERSIONS = Map.of(
    "1.20", "1gn9yrnl",
    "1.19", "013z703q",
    "1.18", "4qy24521",
    "1.17", "mlnno7nl",
    "1.16", "klrw35m1",
    "1.15", "81p687gq",
    "1.14", "gq7r9kdl",
    "1.13", "xqkm97kl",
    "1.12", "81pd4881"
  );

  public static final Map<String, String> AACH_VERSIONS = Map.of(
    "1.8-1.11", "klrzp521",
    "1.0-1.6", "jqz6p0m1"
  );

  public static final Map<String, String> ANY_PERCENT_VERSIONS = Map.of(
    "1.16+", "4qye4731",
    "1.13-1.15", "21go6e6q",
    "1.9-1.12", "jq6j9571",
    "1.8", "q5v9ev2l",
    "pre-1.8", "gq7zo9p1"
  );

  public static final Map<String, String> SEED_TYPES = Map.of(
    "any% rsg", "21d4zvp1",
    "any% ssg", "klrzpjo1",

    "aadv rsg", "01343grl",
    "aadv ssg", "81wr3yml",
    "aadv rs", "zqo2d45l",
    "aadv ss", "5lmwz58q",

    "aach rsg", "4qy8je21",
    "aach ssg", "5q8rd731",
    "aach rs", "9qjgyzgq",
    "aach ss", "810xe551"
  );

  public static Set<Pair<String, String>> downloadedLeaderboards = new HashSet<>();

  private final String category;
  private final String version;

  public SrcLeaderboardRequest(String category, String version) {
    super(getLeaderboardUrl(category, version));
    this.category = category;
    this.version = version;
  }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    Debug.log(
      Debug.REQUEST_SECTION,
      "Requested " + this.category + ' ' + this.version + " leaderboard from speedrun.com"
    );
    this.beginTiming();

    // Download leaderboard from speedrun.com as JSON string
    return HttpUtils.getStringAsync(this.url).thenApply(response -> {
      this.endTiming();

      if (this.handleResponse(response)) {
        downloadedLeaderboards.add(new Pair<>(this.category, this.version));
        return true;
      }

      return false;
    }).exceptionallyCompose(e -> {
      if (e instanceof HttpTimeoutException) {
        Debug.log(
          Debug.REQUEST_SECTION,
          "-- " + this.category + ' ' + this.version + " leaderboard request timed out"
        );
        // Request timed out, nothing left to do here
      } else if (e instanceof IOException) {
        Debug.log(
          Debug.REQUEST_SECTION,
          "-- " + this.category + ' ' + this.version + " leaderboard request failed: " + e.getMessage()
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

    if (!Leaderboard.syncSpeedrunDotComLeaderboard(response, this.category, this.version)) {
      Debug.log(Debug.REQUEST_SECTION, "-- Received invalid " + this.category + " (" + this.version + ") leaderboard data");
      return false;
    }

    Leaderboard.saveSpeedrunDotComLeaderboardToCache(response, this.category, this.version);
    Debug.log(Debug.REQUEST_SECTION, INCOMING + " Received " + this.category + " (" + this.version + ") leaderboard from speedrun.com");
    return true;
  }

  public static String getLeaderboardUrl(String category, String version) {
    category = category.toLowerCase();
    version = version.toLowerCase();
    String seedTypeKey = SEED_TYPES.get(category);
    if (seedTypeKey == null) return "";

    switch (category) {
      case "any% rsg", "any% ssg" -> {
        String versionKey = ANY_PERCENT_VERSIONS.get(version);
        if (versionKey == null) return "";

        return API + "/category/" + ANY_PERCENT_CATEGORY_VAR + "?top=100&embed=players"
             + "&var-" + ANY_PERCENT_VERSION_VAR + '=' + versionKey
             + "&var-" + ANY_PERCENT_SEED_TYPE_VAR + '=' + seedTypeKey;
      }

      case "aadv rsg", "aadv ssg" -> {
        String versionKey = AADV_VERSIONS.get(version);
        if (versionKey == null) return "";

        return API + "/category/" + ALL_ADVANCEMENTS_CATEGORY_VAR + "?top=100&embed=players"
             + "&var-" + AADV_VERSION_VAR + '=' + versionKey
             + "&var-" + AADV_SEED_TYPE_VAR + '=' + seedTypeKey;
      }

      case "aach rsg", "aach ssg" -> {
        String versionKey = AACH_VERSIONS.get(version);
        if (versionKey == null) return "";

        return API + "/category/" + ALL_ACHIEVEMENTS_CATEGORY_VAR + "?top=100&embed=players"
             + "&var-" + AACH_VERSION_VAR + '=' + versionKey
             + "&var-" + AACH_SEED_TYPE_VAR + '=' + seedTypeKey;
      }

      default -> { return ""; }
    }
  }
}
