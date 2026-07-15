package org.mcsr.aatool.net.requests;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.utilities.Pair;

public final class SrcLeaderboardRequest extends NetRequest {
  private static final String API;

  private static final String ANY_PERCENT_CATEGORY_VAR;
  private static final String ANY_PERCENT_VERSION_VAR;
  private static final String ANY_PERCENT_SEED_TYPE_VAR;

  private static final String ALL_ADVANCEMENTS_CATEGORY_VAR;
  private static final String AADV_VERSION_VAR;
  private static final String AADV_SEED_TYPE_VAR;

  private static final String ALL_ACHIEVEMENTS_CATEGORY_VAR;
  private static final String AACH_VERSION_VAR;
  private static final String AACH_SEED_TYPE_VAR;

  public static final Map<String, String> AADV_VERSIONS;

  public static final Map<String, String> AACH_VERSIONS;

  public static final Map<String, String> ANY_PERCENT_VERSIONS;

  public static final Map<String, String> SEED_TYPES;

  public static Set<Pair<String, String>> downloadedLeaderboards;

  private final String category;
  private final String version;

  public SrcLeaderboardRequest(String category, String version) {}

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(String response) {}

  public static String getLeaderboardUrl(String category, String version, int maxRuns/* = 100*/) {}
}
