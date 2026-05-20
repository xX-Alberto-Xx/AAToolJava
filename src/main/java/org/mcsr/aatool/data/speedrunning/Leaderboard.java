package org.mcsr.aatool.data.speedrunning;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utils.Pair;

public final class Leaderboard {
  public static final Map<Pair<String, String>, Leaderboard> ALL_BOARDS;
  private static final Set<Pair<String, String>> LIVE_BOARDS;
  private static final Set<String> REQUESTED_IDENTITIES;

  private static Map<String, String> nickNames;
  private static Map<String, String> realNames;
  private static Map<String, Uuid> identities;

  private static Set<String> allRunnerNames;
  private static Set<Uuid> allRunners;

  private static Map<Pair<String, DateTime>, String> aaLinks;
  private static Leaderboard halfHeartHardcoreCompletions;
  private static List<Run> hundredHardcoreCompletions;
  private static List<Run> listOfMostConcurrentRecords;
  private static String runnerWithMostConcurrentRecords;
  private static int mostConsecutiveRecordsCount;
  private static String runnerWithMostConsecutiveRecords;
  private static LeaderboardSheet history;

  private static String anyRsgRunner;
  private static TimeSpan anyRsgInGameTime;
  private static TimeSpan anyRsgRealTime;

  private static String anySsgRunner;
  private static TimeSpan anySsgInGameTime;
  private static TimeSpan anySsgRealTime;

  private static String aASsgRunner;
  private static TimeSpan aASsgInGameTime;
  private static TimeSpan aASsgRealTime;

  public static final TimeZoneInfo TIME_ZONE;

  public static final String[] AA_VERSIONS;

  public static final String[] ANY_PERCENT_VERSIONS;

  public static boolean secondaryCachesLoaded;

  private String category;
  private String version;

  public Map<String, Integer> ranks;
  public List<Run> runs;

  public static boolean cachedChallengesLoaded;
  public static boolean cachedHistoryLoaded;

  private static boolean nickNamesLoaded;

  public Leaderboard(String category, String version) {}

  public Leaderboard(LeaderboardSheet sheet, String category, String version) {}

  public Leaderboard(LeaderboardSrcJson json, String category, String version) {}

  public static Map<Pair<String, DateTime>, String> getAALinks() { return aaLinks; }
  public static Leaderboard getHalfHeartHardcoreCompletions() { return halfHeartHardcoreCompletions; }
  public static List<Run> getHundredHardcoreCompletions() { return hundredHardcoreCompletions; }
  public static List<Run> getListOfMostConcurrentRecords() { return listOfMostConcurrentRecords; }
  public static String getRunnerWithMostConcurrentRecords() { return runnerWithMostConcurrentRecords; }
  public static int getMostConsecutiveRecordsCount() { return mostConsecutiveRecordsCount; }
  public static String getRunnerWithMostConsecutiveRecords() { return runnerWithMostConsecutiveRecords; }
  public static LeaderboardSheet getHistory() { return history; }

  public static String getAnyRsgRunner() { return anyRsgRunner; }
  public static TimeSpan getAnyRsgInGameTime() { return anyRsgInGameTime; }
  public static TimeSpan getAnyRsgRealTime() { return anyRsgRealTime; }

  public static String getAnySsgRunner() { return anySsgRunner; }
  public static TimeSpan getAnySsgInGameTime() { return anySsgInGameTime; }
  public static TimeSpan getAnySsgRealTime() { return anySsgRealTime; }

  public static String getAASsgRunner() { return aASsgRunner; }
  public static TimeSpan getAASsgInGameTime() { return aASsgInGameTime; }
  public static TimeSpan getAASsgRealTime() { return aASsgRealTime; }

  public final String getCategory() { return this.category; }
  public final String getVersion() { return this.version; }

  public static boolean areNickNamesLoaded() { return nickNamesLoaded; }
  public static Pair<String, String> getCurrent() {}
  public static boolean identityAlreadyRequested(String name) {}
  public static boolean isRunner(Uuid player, String name/* = null*/) {}

  public static boolean isLiveAvailable(String category, String version) {}

  public static void initialize() {}

  public static void refresh(String category/* = null*/, String version/* = null*/) {}

  public static void refreshSrc(String category/* = null*/, String version/* = null*/) {}

  public static String guidanceHeader(String caterogy, String version) {}

  public static boolean tryGetIdentity(String runner, /*out */Uuid uuid) {}

  public static String getRealName(String runner, String fallback/* = null*/) {}

  public static String getNickName(String runner, String fallback/* = null*/) {}

  public static String getKey(String category, String version) {}

  public final void addRun(Run run, int rank) {}

  public static boolean tryGet(String category, String version, /*out */Leaderboard leaderboard) {}

  public static boolean tryGetRank(String runner, String category, String version, /*out */int rank) {}

  public static boolean tryGetRank(Uuid runner, String category, String version, /*out */int rank) {}

  public static boolean tryGetWorldRecord(String category, String version, /*out */Run wr) {}

  public static boolean syncSheetLeaderboard(String sheetId, String pageId, String csv) {}

  public static boolean syncChallengeLeaderboards(String csv) {}

  public static boolean syncHistory(String csv, boolean save) {}

  public static boolean syncNicknames(String csv) {}

  public static boolean syncSpeedrunDotComLeaderboard(String json, String category, String version) {}

  public static boolean syncSpeedrunDotComRecord(String jsonString, boolean rsg, boolean aa) {}

  public static String getPlace(int rank) {}

  public static void saveSpeedrunDotComLeaderboardToCache(String jsonString, String category, String version) {}

  public static void saveSpeedrunDotComRecordToCache(String jsonString, boolean rsg, boolean aa) {}

  private static boolean tryLoadCached(String category, String version, /*out */Leaderboard leaderboard) {}

  public static boolean tryLoadCachedHistory() {}

  public static boolean tryLoadCachedChallenges() {}

  public static boolean tryLoadCachedSrc(String category, String version, /*out */Leaderboard leaderboard) {}

  private static boolean tryLoadCachedSpeedrunDotComRecord(boolean rsg, boolean aa, String version, /*out */String jsonString) {}

  private static void updateMostConcurrentRecords() {}

  private static void updateMostConsecutiveRecords() {}
}
