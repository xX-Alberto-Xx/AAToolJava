package org.mcsr.aatool.data.speedrunning;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAdvancements;
import org.mcsr.aatool.data.categories.Category;
import org.mcsr.aatool.net.Player;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Result;
import org.mcsr.aatool.utilities.Strings;
import org.mcsr.aatool.utilities.Version;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public final class Leaderboard {
  public static final Map<Pair<String, String>, Leaderboard> ALL_BOARDS = new HashMap<>();
  private static final Set<Pair<String, String>> LIVE_BOARDS = new HashSet<>();

  private static Map<String, String> nickNames = new HashMap<>();
  private static Map<String, String> realNames = new HashMap<>();
  private static Map<String, Uuid> identities = new HashMap<>();

  private static Set<String> allRunnerNames = new HashSet<>();
  private static Set<Uuid> allRunners = new HashSet<>();

  private static Map<Pair<String, LocalDate>, String> aaLinks = new HashMap<>();
  private static Leaderboard halfHeartHardcoreCompletions;
  private static List<Run> hundredHardcoreCompletions = new ArrayList<>();
  private static List<Run> listOfMostConcurrentRecords = new ArrayList<>();
  private static String runnerWithMostConcurrentRecords = "";
  private static int mostConsecutiveRecordsCount;
  private static String runnerWithMostConsecutiveRecords = "";
  private static LeaderboardSheet history;

  private static String anyRsgRunner;
  private static Duration anyRsgInGameTime = Duration.ZERO;
  private static Duration anyRsgRealTime = Duration.ZERO;

  private static String anySsgRunner;
  private static Duration anySsgInGameTime = Duration.ZERO;
  private static Duration anySsgRealTime = Duration.ZERO;

  private static String aaSsgRunner;
  private static Duration aaSsgInGameTime = Duration.ZERO;
  private static Duration aaSsgRealTime = Duration.ZERO;

  public static final ZoneId TIME_ZONE = ZoneId.of("-05:00");

  public static final String[] AA_VERSIONS = Stream.concat(
    AllAdvancements.SUPPORTED_VERSIONS.stream(), Stream.of("1.11", "1.6")
  ).toArray(String[]::new);

  public static final String[] ANY_PERCENT_VERSIONS = {
    "1.16+", "1.13-1.15", "1.9-1.12", "1.18", "pre-1.18"
  };

  private static boolean secondaryCachesLoaded;

  private static boolean cachedChallengesLoaded;
  private static boolean cachedHistoryLoaded;

  private static boolean nickNamesLoaded;

  private String category;
  private String version;

  public Map<String, Integer> ranks = new HashMap<>();
  public List<Run> runs = new ArrayList<>();

  public Leaderboard(String category, String version) {
    this.category = category;
    this.version = version;
  }

  public Leaderboard(LeaderboardSheet sheet, String category, String version) {
    this(category, version);

    synchronized (this.ranks) {
      int numRows = sheet.getRows().length;

      switch (category) {
        case "Hardcore No Reset" -> {
          for (int i = 2; i < numRows; i++) {
            HardcoreStreak run = HardcoreStreak.tryParse(sheet, i, this.version);
            if (run == null) continue;

            int rank = run.bestStreak >= 100 ? 1 : run.bestStreak >= 50 ? 2 : Integer.MAX_VALUE;
            this.addRun(run, rank);

            if (rank == 1) hundredHardcoreCompletions.add(run);
          }
        }

        case "All Versions" -> {
          for (int i = 2; i < numRows; i++) {
            AllVersionsRun run = AllVersionsRun.tryParse(sheet, i, this.version);
            if (run != null) this.addRun(run, i - 1);
          }
        }

        default -> {
          for (int i = 2; i < numRows; i++) {
            Run run = Run.tryParse(sheet, i, this.version);
            if (run != null) this.addRun(run, i - 1);
          }

          if ("1K No Reset".equals(category)) {
            for (Run run : this.runs) run.comment = "1K No Reset";
          }
        }
      }
    }
  }

  public Leaderboard(LeaderboardSrcJson json, String category, String version) {
    this(category, version);

    synchronized (this.ranks) {
      for (int i = 0; i < json.runs.size(); i++) {
        this.addRun(json.runs.get(i), i + 1);
      }
    }
  }

  public static Map<Pair<String, LocalDate>, String> getAALinks() { return aaLinks; }
  public static Leaderboard getHalfHeartHardcoreCompletions() { return halfHeartHardcoreCompletions; }
  public static List<Run> getHundredHardcoreCompletions() { return hundredHardcoreCompletions; }
  public static List<Run> getListOfMostConcurrentRecords() { return listOfMostConcurrentRecords; }
  public static String getRunnerWithMostConcurrentRecords() { return runnerWithMostConcurrentRecords; }
  public static int getMostConsecutiveRecordsCount() { return mostConsecutiveRecordsCount; }
  public static String getRunnerWithMostConsecutiveRecords() { return runnerWithMostConsecutiveRecords; }
  public static LeaderboardSheet getHistory() { return history; }

  public static String getAnyRsgRunner() { return anyRsgRunner; }
  public static Duration getAnyRsgInGameTime() { return anyRsgInGameTime; }
  public static Duration getAnyRsgRealTime() { return anyRsgRealTime; }

  public static String getAnySsgRunner() { return anySsgRunner; }
  public static Duration getAnySsgInGameTime() { return anySsgInGameTime; }
  public static Duration getAnySsgRealTime() { return anySsgRealTime; }

  public static String getAASsgRunner() { return aaSsgRunner; }
  public static Duration getAASsgInGameTime() { return aaSsgInGameTime; }
  public static Duration getAASsgRealTime() { return aaSsgRealTime; }

  public String getCategory() { return this.category; }
  public String getVersion() { return this.version; }

  public static boolean areNickNamesLoaded() { return nickNamesLoaded; }

  public static Pair<String, String> getCurrent() {
    Category category = Tracker.getCategory();
    return new Pair<>(category.getName(), category.getCurrentMajorVersion());
  }

  public static boolean isRunner(Uuid player) { return isRunner(player, null); }
  public static boolean isRunner(Uuid player, String name) {
    return allRunners.contains(player) || allRunnerNames.contains(name);
  }

  public static boolean isLiveAvailable(String category, String version) {
    return "HHHAA".equals(category)
           ? history != null
           : LIVE_BOARDS.contains(new Pair<>(category, version));
  }

  public static void initialize() {
    for (String version : AA_VERSIONS) tryLoadCached("All Advancements", version);
    for (String version : AA_VERSIONS) tryLoadCached("All Advancements", version);

    tryLoadCachedSpeedrunDotComRecord(true, false, "1.16");
    tryLoadCachedSpeedrunDotComRecord(false, false, "1.16");
    tryLoadCachedSpeedrunDotComRecord(false, true, "1.16");
    tryLoadCachedChallenges();
    updateMostConcurrentRecords();
    updateMostConsecutiveRecords();
  }

  public static void refresh() { refresh(null, null); }
  public static void refresh(String category) { refresh(category, null); }
  public static void refresh(String category, String version) {
    // TODO: NetRequest
    Player.IDENTITIES_ALREADY_REQUESTED.clear();
    Player.NAMES_ALREADY_REQUESTED.clear();
    // TODO: SpreadsheetRequest
    // TODO: Badge
    nickNamesLoaded = false;

    if (category == null) LIVE_BOARDS.clear();
    else LIVE_BOARDS.remove(new Pair<>(category, version));
  }

  public static void refreshSrc() { refreshSrc(null, null); }
  public static void refreshSrc(String category) { refreshSrc(category, null); }
  public static void refreshSrc(String category, String version) {
    // TODO: NetRequest
    Player.IDENTITIES_ALREADY_REQUESTED.clear();
    Player.NAMES_ALREADY_REQUESTED.clear();
    // TODO: Badge
    nickNamesLoaded = false;

    if (category == null) {
      // TODO: SrcLeaderboardRequest
      LIVE_BOARDS.clear();
    } else {
      // TODO: SrcLeaderboardRequest
      LIVE_BOARDS.remove(new Pair<>(category, version));
    }
  }

  public static String guidanceHeader(String category, String version) {
    // 1.16 AA has its own separate page
    return switch (category) {
      case "All Advancements" -> "1.16".equals(version) ? null : version + " rsg";
      case "All Blocks" -> null;
      default -> version + " rsg";
    };
  }

  public static Uuid tryGetIdentity(String runner) {
    return !Strings.isNullOrEmpty(runner) ? identities.getOrDefault(runner, Uuid.EMPTY) : Uuid.EMPTY;
  }

  public static String getRealName(String runner) { return getRealName(runner, null); }
  public static String getRealName(String runner, String fallback) {
    if (fallback == null) fallback = runner;

    return Strings.isNullOrEmpty(runner)
           ? fallback
           : realNames.getOrDefault(runner.toLowerCase(), fallback);
  }

  public static String getNickName(String runner) { return getNickName(runner, null); }
  public static String getNickName(String runner, String fallback) {
    if (fallback == null) fallback = runner;

    return Strings.isNullOrEmpty(runner)
           ? fallback
           : nickNames.getOrDefault(runner.toLowerCase(), fallback);
  }

  public static String getKey(String category, String version) {
    return switch (category) {
      case "All Blocks" -> "leaderboard_all_blocks_" + version;
      case "Challenge" -> "leaderboard_challenges";
      default -> "1.16".equals(version) ? "leaderboard_aa_primary" : "leaderboard_aa_others";
    };
  }

  public void addRun(Run run, int rank) {
    this.runs.add(run);
    this.ranks.put(run.runner.toLowerCase(), rank);

    String realName = getRealName(run.runner);
    allRunnerNames.add(realName);
    allRunnerNames.add(run.runner);

    Uuid id = Player.tryGetUuid(realName);
    if (id != null) allRunners.add(id);
  }

  public static Leaderboard tryGet(String category, String version) {
    if ("HHHAA".equals(category)) return halfHeartHardcoreCompletions;

    Pair<String, String> key = new Pair<>(category, version);
    return ALL_BOARDS.containsKey(key) ? ALL_BOARDS.get(key) : tryLoadCached(category, version);
  }

  public static Result<Integer> tryGetRank(String runner, String category, String version) {
    if (Strings.isNullOrEmpty(runner)) return new Result<>(false, 0);

    Leaderboard board = ALL_BOARDS.get(new Pair<>(category, version));
    if (board == null) return new Result<>(false, 0);

    int ignRank, nickRank;

    synchronized (board.ranks) {
      ignRank = board.ranks.get(getRealName(runner).toLowerCase());
      nickRank = board.ranks.get(getNickName(runner).toLowerCase());
    }

    int rank = ignRank > 0 && nickRank > 0 ? Math.min(ignRank, nickRank)
             : ignRank + nickRank > 0 ? Math.max(ignRank, nickRank)
             : 0;
    return new Result<>(rank != Integer.MAX_VALUE, rank);
  }

  public static Result<Integer> tryGetRank(Uuid runner, String category, String version) {
    if (runner.equals(Uuid.EMPTY)) return new Result<>(false, 0);

    Leaderboard board = ALL_BOARDS.get(new Pair<>(category, version));
    if (board == null) return new Result<>(false, 0);

    int ignRank, nickRank;

    synchronized (board.ranks) {
      ignRank = board.ranks.get(getRealName(runner.string).toLowerCase());
      nickRank = board.ranks.get(getNickName(runner.string).toLowerCase());
    }

    int rank = ignRank > 0 && nickRank > 0 ? Math.min(ignRank, nickRank)
             : ignRank + nickRank > 0 ? Math.max(ignRank, nickRank)
             : 0;
    return new Result<>(rank != Integer.MAX_VALUE, rank);
  }

  public static Run tryGetWorldRecord(String category, String version) {
    if (Strings.isNullOrEmpty(category) || Strings.isNullOrEmpty(version)) return null;

    Leaderboard board = ALL_BOARDS.get(new Pair<>(category, version));
    return board == null || board.runs.isEmpty() ? null : board.runs.get(0);
  }

  public static boolean syncSheetLeaderboard(String sheetId, String pageId, String csv) {
    String category;
    switch (sheetId) {
      case Paths.Web.AA_SHEET -> category = "All Advancements";
      case Paths.Web.AB_SHEET -> category = "All Blocks";
      default -> { return false; }
    }

    List<String> versions =
      Paths.Web.AA_SHEET.equals(sheetId) && Paths.Web.AA_PAGE16.equals(pageId) ||
      Paths.Web.AB_SHEET.equals(sheetId) && Paths.Web.AB_PAGE16.equals(pageId)
      ? List.of("1.16")
      : switch (pageId) {
        case Paths.Web.AB_PAGE18 -> List.of("1.18");
        case Paths.Web.AB_PAGE19 -> List.of("1.19");
        case Paths.Web.AB_PAGE20 -> List.of("1.20");
        case Paths.Web.AB_PAGE21 -> List.of("1.21");
        default -> Stream.concat(
          AllAdvancements.SUPPORTED_VERSIONS.stream().filter(v -> !"1.16".equals(v)),
          Stream.of("1.11", "1.6")
        ).toList();
      };

    LeaderboardSheet sheet = null;

    // Parse all the leaderboards
    for (String version : versions) {
      sheet = LeaderboardSheet.tryParse(
        csv,
        getKey(category, version),
        guidanceHeader(category, version)
      );

      if (sheet != null) {
        Pair<String, String> key = new Pair<>(category, version);
        ALL_BOARDS.put(key, new Leaderboard(sheet, category, version));
        LIVE_BOARDS.add(key);
      }
    }

    updateMostConcurrentRecords();
    if (sheet != null) sheet.saveToCache();
    return sheet != null;
  }

  public static boolean syncChallengeLeaderboards(String csv) {
    LeaderboardSheet sheet = null;

    for (String challenge : new String[] { "Hardcore No Reset", "1K No Reset", "All Items", "All Versions" }) {
      sheet = LeaderboardSheet.tryParse(
        csv,
        getKey("Challenge", ""),
        challenge.toLowerCase()
      );

      if (sheet != null) {
        Pair<String, String> key = new Pair<>(challenge, "1.16");
        ALL_BOARDS.put(key, new Leaderboard(sheet, challenge, "1.16"));
        LIVE_BOARDS.add(key);
      }
    }

    if (sheet != null) sheet.saveToCache();
    return sheet != null;
  }

  public static boolean syncHistory(String csv, boolean save) {
    LeaderboardSheet sheet = LeaderboardSheet.tryParse(csv, "history_aa_1.16", null);
    if (sheet == null) return false;

    history = sheet;
    aaLinks.clear();
    List<Run> hhhRuns = new ArrayList<>();
    int numRows = history.getRows().length;

    for (int i = 1; i < numRows; i++) {
      String runner = history.tryGetRunner(i);
      if (runner.isEmpty()) continue;

      LocalDate date = history.tryGetDate(i);
      if (date == null) continue;

      String link = history.tryGetLink(i);
      if (!link.isEmpty()) aaLinks.put(new Pair<>(runner, date), link);

      if (!history.tryGetComment(i).equals("Modded (HHH mod)")) continue;

      Duration igt = history.tryGetIgt(i);
      if (igt == null) continue;

      Run run = new Run();
      run.runner = runner;
      run.date = date;
      run.inGameTime = igt;

      Duration rta = history.tryGetRta(i);
      run.realTime = rta != null ? rta : Duration.ZERO;
      run.setLink(link);
      hhhRuns.add(run);
    }

    hhhRuns.sort((x, y) -> x.inGameTime.compareTo(y.inGameTime));

    if (!hhhRuns.isEmpty()) {
      halfHeartHardcoreCompletions = new Leaderboard("HHHAA", "1.16");

      for (int i = 0; i < hhhRuns.size(); i++) {
        halfHeartHardcoreCompletions.addRun(hhhRuns.get(i), i + 1);
      }
    }

    if (save) sheet.saveToCache();
    return true;
  }

  public static boolean syncNicknames(String csv) {
    NicknameSheet sheet = NicknameSheet.tryParse(csv);

    if (sheet != null) {
      NicknameSheet.Mappings mappings = sheet.getMappings();
      realNames = mappings.realNames;
      nickNames = mappings.nickNames;
      identities = mappings.identities;
      nickNamesLoaded = true;
      sheet.saveToCache();
    }

    return nickNamesLoaded;
  }

  public static boolean syncSpeedrunDotComLeaderboard(String json, String category, String version) {
    LeaderboardSrcJson valid = LeaderboardSrcJson.tryParse(json, version);
    if (valid == null) return false;

    Pair<String, String> key = new Pair<>(category, version);
    ALL_BOARDS.put(key, new Leaderboard(valid, category, version));
    LIVE_BOARDS.add(key);
    return true;
  }

  public static boolean syncSpeedrunDotComRecord(String jsonString, boolean rsg, boolean aa) {
    try {
      JsonObject data = JsonUtils.STRICT_GSON.fromJson(jsonString, JsonObject.class).getAsJsonObject("data");
      String runner = data
        .getAsJsonObject("players")
        .getAsJsonArray("data")
        .get(0).getAsJsonObject()
        .getAsJsonObject("names")
        .getAsJsonPrimitive("international").getAsString();
      // TODO: AvatarRequest

      JsonObject times = data
        .getAsJsonArray("runs")
        .get(0).getAsJsonObject()
        .getAsJsonObject("run")
        .getAsJsonObject("times");

      Duration igt = Duration.ofMillis(Math.round(
        times.getAsJsonPrimitive("ingame_t").getAsDouble() * 1000
      ));

      Duration rta = Duration.ofMillis(Math.round(
        times.getAsJsonPrimitive("realtime_t").getAsDouble() * 1000
      ));

      if (rsg) {
        anyRsgRunner = runner;
        anyRsgInGameTime = igt;
        anyRsgRealTime = rta;
      } else if (aa) {
        aaSsgRunner = runner;
        aaSsgInGameTime = igt;
        aaSsgRealTime = rta;
      } else {
        anySsgRunner = runner;
        anySsgInGameTime = igt;
        anySsgRealTime = rta;
      }

      return true;
    } catch (
      JsonSyntaxException | NullPointerException | ClassCastException |
      IndexOutOfBoundsException | IllegalStateException | NumberFormatException ignored
    ) {
      return false;
    }
  }

  public static String getPlace(int rank) {
    return rank + switch (rank % 100) {
      case 11, 12, 13 -> "th";
      default -> switch (rank % 10) {
        case 1 -> "st";
        case 2 -> "nd";
        case 3 -> "rd";
        default -> "th";
      };
    };
  }

  public static void saveSpeedrunDotComLeaderboardToCache(String jsonString, String category, String version) {
    try {
      // Cache leaderboard so it loads instantly next launch
      // Overwrite to keep leaderboard up to date
      Files.createDirectories(Paths.System.LEADERBOARDS_FOLDER);
      Files.writeString(Paths.System.speedrunDotComLeaderboardFile(category, version), jsonString);
    } catch (IOException ignored) {
      // Couldn't save file. Ignore and move on
    }
  }

  public static void saveSpeedrunDotComRecordToCache(String jsonString, boolean rsg, boolean aa) {
    try {
      // Cache leaderboard so it loads instantly next launch
      // Overwrite to keep leaderboard up to date
      Files.createDirectories(Paths.System.LEADERBOARDS_FOLDER);
      Files.writeString(Paths.System.speedrunDotComRecordFile(rsg, aa, "1.16"), jsonString);
    } catch (IOException ignored) {
      // Couldn't save file. Ignore and move on
    }
  }

  private static Leaderboard tryLoadCached(String category, String version) {
    if (category.toLowerCase().contains("any%")) {
      return tryLoadCachedSrc(category, version);
    }

    if (category.equals("1K No Reset")) {
      return tryLoadCachedChallenges() ? ALL_BOARDS.get(new Pair<>(category, "1.16")) : null;
    }

    Leaderboard leaderboard = null;

    try {
      String key = getKey(category, version);
      LeaderboardSheet sheet = LeaderboardSheet.tryParse(
        Files.readString(Paths.System.leaderboardFile(key)),
        key,
        guidanceHeader(category, version)
      );

      if (sheet != null) leaderboard = new Leaderboard(sheet, category, version);
      ALL_BOARDS.put(new Pair<>(category, version), leaderboard);
    } catch (IOException ignored) {
      // Couldn't read cached leaderboard, move on
    }

    try {
      NicknameSheet sheet = NicknameSheet.tryParse(
        Files.readString(Paths.System.leaderboardFile("leaderboard_names"))
      );

      if (sheet != null) {
        NicknameSheet.Mappings mappings = sheet.getMappings();
        realNames = mappings.realNames;
        nickNames = mappings.nickNames;
        identities = mappings.identities;
      }
    } catch (IOException ignored) {
      // Couldn't read cached nickname mappings, move on
    }

    return leaderboard;
  }

  public static boolean tryLoadCachedHistory() {
    if (cachedHistoryLoaded) return false;
    cachedHistoryLoaded = true;

    try {
      return syncHistory(Files.readString(Paths.System.getHistoryFile()), false);
    } catch (IOException ignored) {
      // Couldn't read cached history, move on
      return false;
    }
  }

  public static boolean tryLoadCachedChallenges() {
    if (cachedChallengesLoaded) return false;
    cachedChallengesLoaded = true;

    try {
      return syncChallengeLeaderboards(Files.readString(Paths.System.getChallengesFile()));
    } catch (IOException ignored) {
      // Couldn't read cached history, move on
      return false;
    }
  }

  public static Leaderboard tryLoadCachedSrc(String category, String version) {
    try {
      LeaderboardSrcJson sheet = LeaderboardSrcJson.tryParse(
        Files.readString(Paths.System.speedrunDotComLeaderboardFile(category, version)),
        version
      );

      Leaderboard leaderboard = sheet != null ? new Leaderboard(sheet, category, version) : null;
      ALL_BOARDS.put(new Pair<>(category, version), leaderboard);
      return leaderboard;
    } catch (IOException ignored) {
      // Couldn't read cached leaderboard, move on
      return null;
    }
  }

  private static String tryLoadCachedSpeedrunDotComRecord(boolean rsg, boolean aa, String version) {
    try {
      String jsonString = Files.readString(Paths.System.speedrunDotComRecordFile(rsg, aa, version));
      syncSpeedrunDotComRecord(jsonString, rsg, aa);
      return jsonString;
    } catch (IOException ignored) {
      // Couldn't read cached leaderboard, move on
      return "";
    }
  }

  private static void updateMostConcurrentRecords() {
    Map<String, List<Run>> recordHolders = new HashMap<>();

    for (String version : AA_VERSIONS) {
      Run wr = tryGetWorldRecord("All Advancements", version);
      if (wr == null) continue;

      List<Run> records = recordHolders.get(wr.runner);

      if (records == null) {
        records = new ArrayList<>();
        recordHolders.put(wr.runner, records);
      }

      records.add(wr);
    }

    int mostRecordsCount = 0;
    Version mostLatestVersion = null;

    for (List<Run> recordHolderRuns : recordHolders.values()) {
      int count = recordHolderRuns.size();
      if (count < mostRecordsCount) continue;

      Run newestVersion = recordHolderRuns.get(0);

      if (count > mostRecordsCount || newestVersion.gameVersion.isAfter(mostLatestVersion)) {
        runnerWithMostConcurrentRecords = newestVersion.runner;
        mostRecordsCount = count;
        mostLatestVersion = newestVersion.gameVersion;
      }
    }

    listOfMostConcurrentRecords.clear();
    listOfMostConcurrentRecords.addAll(recordHolders.get(runnerWithMostConcurrentRecords));
  }

  private static void updateMostConsecutiveRecords() {
    tryLoadCachedHistory();
    if (history == null) return;

    int numRows = history.getRows().length;
    if (numRows == 0) return;

    Duration wr = ChronoUnit.FOREVER.getDuration();
    int currentRecords = 0;
    String currentRunner = null;
    mostConsecutiveRecordsCount = 0;
    runnerWithMostConsecutiveRecords = null;

    for (int row = 1; row < numRows; row++) {
      Run run = Run.tryParse(history, row, "1.16");
      if (run == null || run.inGameTime.compareTo(wr) > 0) continue;

      wr = run.inGameTime;

      if (run.runner.equals(currentRunner)) {
        currentRecords++;
      } else {
        currentRunner = run.runner;
        currentRecords = 1;
      }

      if (currentRecords > mostConsecutiveRecordsCount) {
        mostConsecutiveRecordsCount = currentRecords;
        runnerWithMostConsecutiveRecords = currentRunner;
      }
    }
  }
}
