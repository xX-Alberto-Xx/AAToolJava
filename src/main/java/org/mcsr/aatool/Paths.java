package org.mcsr.aatool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.configuration.TrackingConfig;
import org.mcsr.aatool.enums.TrackerSource;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.ActiveInstance;
import org.mcsr.aatool.utilities.OperatingSystem;

public final class Paths {
  public static final Path EMPTY_PATH = Path.of("");

  private Paths() {}

  public static Stream<Path> tryGetAllFiles(Path path, String pattern, boolean recurse) {
    try {
      PathMatcher matcher = path.getFileSystem().getPathMatcher("glob:" + pattern);

      return Files.find(
        path,
        recurse ? Integer.MAX_VALUE : 1,
        (p, attrs) -> !attrs.isDirectory() && matcher.matches(p.getFileName())
      );
    } catch (IllegalArgumentException | IOException ignored) {
      return null;
    }
  }

  public static boolean isNullOrEmpty(Path path) { return path == null || path.equals(EMPTY_PATH); }

  public static String getFileNameWithoutExtension(Path file) {
    String fileName = file.getFileName().toString();
    int dotIndex = fileName.lastIndexOf('.');
    return dotIndex != -1 ? fileName.substring(0, dotIndex) : fileName;
  }

  public static final class System {
    // Constant settings paths
    public static final Path CONFIG_FOLDER = Path.of("config");
    public static final Path NOTES_FOLDER = Path.of("notes");

    public static final Path CACHE_FOLDER = Path.of("assets", "cache");
    public static final Path LEADERBOARDS_FOLDER = CACHE_FOLDER.resolve("leaderboards");
    public static final Path BLOCK_CHECKLISTS_FOLDER = CACHE_FOLDER.resolve("block_checklists");
    private static final Path PROFILES_CACHE_FOLDER = CACHE_FOLDER.resolve("runner_profiles");
    public static final Path PROFILE_PICTURES_CACHE_FOLDER = PROFILES_CACHE_FOLDER.resolve("pictures");
    public static final Path PROFILE_DETAILS_CACHE_FOLDER = PROFILES_CACHE_FOLDER.resolve("details");
    // Remote world temp folder
    public static final Path SFTP_WORLDS_FOLDER = CACHE_FOLDER.resolve("sftp_worlds");

    public static final Path MANUAL_CHECKLIST_FOLDER = Path.of("checklists");

    // Constant assets paths
    public static final Path LOGS_FOLDER = Path.of("logs");
    public static final Path ASSETS_FOLDER = Path.of("assets");
    public static final Path OBJECTIVES_FOLDER = ASSETS_FOLDER.resolve("objectives");
    public static final Path VIEWS_FOLDER = ASSETS_FOLDER.resolve("views");
    public static final Path TEMPLATES_FOLDER = ASSETS_FOLDER.resolve("templates");
    public static final Path SPRITES_FOLDER = ASSETS_FOLDER.resolve("sprites");
    public static final Path FONTS_FOLDER = ASSETS_FOLDER.resolve("fonts");
    public static final Path AVATAR_CACHE_FOLDER = SPRITES_FOLDER.resolve("global").resolve("avatar_cache");
    public static final String WINFORMS_ASSETS; // TODO

    public static final String MAIN_ICON; // TODO
    public static final String UPDATE_ICON; // TODO

    // Constant URLs
    public static final String UPDATE_EXECUTABLE; // TODO

    private static final DateTimeFormatter CRASH_FILE_DATETIME_FORMAT =
      DateTimeFormatter.ofPattern("uuuu_M_dd_H_mm_ss", Locale.ROOT);

    private System() {}

    // Dependant paths

    public static Path getObjectiveFolder() {
      return OBJECTIVES_FOLDER.resolve(Config.getTracking().gameVersion.getValue());
    }

    public static Path getAdvancementsFolder() { return getObjectiveFolder().resolve("advancements"); }
    public static Path getAchievementsFile() { return getObjectiveFolder().resolve("achievements.json"); }
    public static Path getDeathMessagesFile() { return getObjectiveFolder().resolve("deaths.json"); }
    public static Path getArmorTrimsFile() { return getObjectiveFolder().resolve("trims.json"); }
    public static Path getPotionsFile() { return getObjectiveFolder().resolve("potions.json"); }

    // File getters
    public static Path getCrashLogFile() {
      return LOGS_FOLDER.resolve("crash_report_" + CRASH_FILE_DATETIME_FORMAT.format(Instant.now()) + ".txt");
    }

    public static Path getHistoryFile() {
      return LEADERBOARDS_FOLDER.resolve("history_aa_1.16.csv");
    }

    public static Path getChallengesFile() {
      return LEADERBOARDS_FOLDER.resolve("leaderboard_challenges.csv");
    }

    public static Path getSupportersFile() {
      return LEADERBOARDS_FOLDER.resolve("supporters.csv");
    }

    public static Path leaderboardFile(String fileName) {
      return LEADERBOARDS_FOLDER.resolve(fileName + ".csv");
    }

    public static Path blockChecklistFile(int instance, String worldName) {
      return BLOCK_CHECKLISTS_FOLDER.resolve(
        instance < 1
        ? worldName + ".txt"
        : "instance_" + instance + '-' + worldName + ".txt"
      );
    }

    public static Path speedrunDotComLeaderboardFile(String category, String version) {
      return LEADERBOARDS_FOLDER.resolve("speedrundotcom_leaderboard_" + category + '_' + version + ".json");
    }

    public static Path speedrunDotComRecordFile(boolean rsg, boolean aa, String version) {
      return LEADERBOARDS_FOLDER.resolve(
        (aa ? "aa_wr_ssg_" : rsg ? "any_percent_wr_rsg_" : "any_percent_wr_ssg_")
        + version + ".txt"
      );
    }

    public static Path speedrunDotComProfilePicture(String id) {
      return PROFILE_PICTURES_CACHE_FOLDER.resolve(id + ".png");
    }

    public static Path speedrunDotComProfileJson(String idOrName) {
      return PROFILE_DETAILS_CACHE_FOLDER.resolve(idOrName + ".json");
    }
  }

  public static final class Saves {
    private static final Path APP_DATA_SHORTCUT =
      OperatingSystem.CURRENT == OperatingSystem.WINDOWS ? Path.of("%AppData%", "Roaming") : null;
    private static final Path APP_DATA_FOLDER_PATH =
      APP_DATA_SHORTCUT != null ? Path.of(java.lang.System.getenv("AppData")) : null;

    public static final Path MINECRAFT = switch (OperatingSystem.CURRENT) {
      case WINDOWS -> APP_DATA_FOLDER_PATH.resolve(".minecraft");
      case MAC_OS -> Path.of(java.lang.System.getProperty("user.home"), "Library", "Application Support", "minecraft");
      case LINUX -> Path.of(java.lang.System.getProperty("user.home"), ".minecraft");
    };

    private Saves() {}

    public static Path currentFolder() {
      TrackingConfig trackingConfig = Config.getTracking();
      if (trackingConfig.useSftp.getValue()) return System.SFTP_WORLDS_FOLDER;
      if (Tracker.getSource() != TrackerSource.CUSTOM_SAVES_PATH) return ActiveInstance.getSavesPath();

      Path customSavesPath = trackingConfig.customSavesPath.getValue();
      return APP_DATA_SHORTCUT != null && customSavesPath.startsWith(APP_DATA_SHORTCUT)
             ? customSavesPath.getNameCount() == APP_DATA_SHORTCUT.getNameCount()
               ? APP_DATA_FOLDER_PATH
               : APP_DATA_FOLDER_PATH.resolve(customSavesPath.subpath(
                   APP_DATA_SHORTCUT.getNameCount(), customSavesPath.getNameCount()
                 ))
             : customSavesPath;
    }

    public static Path currentPracticeSavesFolder() {
      return Config.getTracking().useSftp.getValue() || Tracker.getSource() == TrackerSource.CUSTOM_SAVES_PATH
             ? EMPTY_PATH
             : ActiveInstance.getPracticeSavesPath();
    }

    public static boolean mightBeWorldFolder(Path folder) {
      return Files.isRegularFile(folder.resolve("level.dat"))
          || Files.isDirectory(folder.resolve("advancements"))
          || Files.isDirectory(folder.resolve("stats"));
    }
  }

  public static final class Web {
    public static final String LATEST_RELEASE = "https://github.com/xX-Alberto-Xx/AAToolJava/releases/latest";
    public static final String OBS_HELP = "https://github.com/DarwinBaker/AATool/blob/main/info/obs.md";
    public static final String PATREON_FULL = "https://www.patreon.com/_ctm";
    public static final String PATREON_SHORT = "Patreon.com/_CTM";

    public static final String PAYPAL = "https://www.paypal.com/donate/?hosted_button_id=EN29468P8CY24";

    public static final String AA_SHEET = "107ijqjELTQQ29KW4phUmtvYFTX9-pfHsjb18TKoWACk";
    public static final String AA_PAGE16 = "1706556435";
    public static final String AA_PAGE_OTHERS = "1283472797";

    public static final String AB_SHEET = "1RnN6lE3yi5S_5PBuxMXdWNvN3HayP3054M3Qud_p9BU";
    public static final String AB_PAGE21 = "27712269";
    public static final String AB_PAGE20 = "1664598957";
    public static final String AB_PAGE19 = "1912774860";
    public static final String AB_PAGE18 = "1706556435";
    public static final String AB_PAGE16 = "1572184167";
    public static final String AB_PAGE_CHALLENGES = "2045031868";

    public static final String SUPPORTER_SHEET = "1Vj1e2kREWuw8XzMu6OazHmbvC-QXCVBH08CaQXnrOD4";
    public static final String NICKNAME_SHEET = "16VS6VkitZdyrfVAFd-UdkVSrXO0nhdMyNeueIFoqvZY";
    public static final String PRIMARY_AA_HISTORY = "735237004";

    public static final String ANY_RSG_RECORD = "https://www.speedrun.com/api/v1/leaderboards/j1npme6p/category/mkeyl926?top=1&embed=players&var-jlzkwql2=mln68v0q&var-r8rg67rn=21d4zvp1";
    public static final String ANY_SSG_RECORD = "https://www.speedrun.com/api/v1/leaderboards/j1npme6p/category/mkeyl926?top=1&embed=players&var-wl33kewl=4qye4731&var-r8rg67rn=klrzpjo1";
    public static final String AA_SSG_RECORD = "https://www.speedrun.com/api/v1/leaderboards/j1npme6p/category/xk9gz16d?top=1&embed=players&var-38do09zl=5q8rd731&var-r8rg67rn=klrzpjo1";

    private Web() {}

    public static String getUuidUrl(String name) {
      return "https://api.mojang.com/users/profiles/minecraft/" + name;
    }

    public static String getNameUrl(String uuid) {
      return "https://api.mojang.com/user/profile/" + uuid.replace("-", "");
    }

    public static String getAvatarUrlFallback(Uuid uuid, int size) {
      return "https://crafatar.com/avatars/" + uuid + "?size=" + size + "&overlay=true";
    }

    public static String getAvatarUrl(Uuid uuid, int size) {
      return "https://minotar.net/helm/" + uuid.shortString + '/' + size;
    }

    public static String getAvatarUrl(String name, int size) {
      return "https://minotar.net/helm/" + name.strip() + '/' + size;
    }

    public static String getSpreadsheetUrl(String sheet, String page) {
      return "https://docs.google.com/spreadsheets/d/" + sheet + "/export?gid=" + page + "&format=csv";
    }

    public static String getAnyPercentRecordUrl(boolean rsg) { return rsg ? ANY_RSG_RECORD : ANY_SSG_RECORD; }
  }
}
