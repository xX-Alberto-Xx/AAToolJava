package org.mcsr.aatool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Stream;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.OperatingSystem;

public final class Paths {
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

  public static final class System {
    // Constant settings paths
    public static final Path CONFIG_FOLDER = Path.of("config");
    public static final String LEGACY_SETTINGS_FOLDER;
    public static final String ARCHIVED_CONFIG_FOLDER;
    public static final String NOTES_FOLDER;

    public static final Path CACHE_FOLDER = Path.of("assets", "cache");
    public static final Path LEADERBOARDS_FOLDER = CACHE_FOLDER.resolve("leaderboards");
    public static final Path BLOCK_CHECKLISTS_FOLDER = CACHE_FOLDER.resolve("block_checklists");
    private static final Path PROFILES_CACHE_FOLDER = CACHE_FOLDER.resolve("runner_profiles");
    public static final Path PROFILE_PICTURES_CACHE_FOLDER = PROFILES_CACHE_FOLDER.resolve("pictures");
    public static final Path PROFILE_DETAILS_CACHE_FOLDER = PROFILES_CACHE_FOLDER.resolve("details");
    // Remote world temp folder
    public static final String SFTP_WORLDS_FOLDER;

    public static final String MANUAL_CHECKLIST_FOLDER;

    // Constant assets paths
    public static final String LOGS_FOLDER;
    public static final Path ASSETS_FOLDER = Path.of("assets");
    public static final Path OBJECTIVES_FOLDER = ASSETS_FOLDER.resolve("objectives");
    public static final String VIEWS_FOLDER;
    public static final String TEMPLATES_FOLDER;
    public static final String SPRITES_FOLDER;
    public static final String FONTS_FOLDER;
    public static final String AVATAR_CACHE_FOLDER;
    public static final String CREDITS_FOLDER;
    public static final String WINFORMS_ASSETS;

    public static final String MAIN_ICON;
    public static final String UPDATE_ICON;

    // Constant URLs
    public static final String UPDATE_EXECUTABLE;

    private System() {}

    // Dependant paths

    public static Path getObjectiveFolder() {
      return OBJECTIVES_FOLDER.resolve(Config.getTracking().gameVersion.getValue());
    }

    public static Path getAdvancementsFolder() { return getObjectiveFolder().resolve("advancements"); }
    public static Path getAchievementsFile() { return getObjectiveFolder().resolve("achievements.json"); }
    public static Path getDeathMessagesFile() { return getObjectiveFolder().resolve("deaths.json"); }
    public static Path getArmorTrimsFile() { return getObjectiveFolder().resolve("trims.json"); }
    public static String getPotionsFile() {}

    // File getters
    public static String getCrashLogFile() {}
    public static String getCreditsFile() {}

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

    public static String blockChecklistFile(int instance, String worldName) {}

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
    public static final Path MINECRAFT = switch (OperatingSystem.CURRENT) {
      case WINDOWS -> Path.of(java.lang.System.getenv("AppData"), ".minecraft");
      case MAC_OS -> Path.of(java.lang.System.getProperty("user.home"), "Library", "Application Support", "minecraft");
      case LINUX -> Path.of(java.lang.System.getProperty("user.home"), ".minecraft");
    };

    public static final String APP_DATA_SHORTCUT;
    private static final String APP_DATA_FOLDER_PATH;

    private Saves() {}

    public static String currentFolder() {}

    public static String currentPracticeSavesFolder() {}

    public static String getDefaultAppDataSavesPath() {}

    public static boolean mightBeWorldFolder(DirectoryInfo folder) {}

    public static DirectoryInfo mostRecentlyWritten(DirectoryInfo a, DirectoryInfo b) {}
  }

  public static final class Web {
    public static final String LATEST_RELEASE;
    public static final String OBS_HELP;
    public static final String PATREON_FULL;
    public static final String PATREON_SHORT;

    public static final String PAY_PAL;

    public static final String AA_SHEET = "107ijqjELTQQ29KW4phUmtvYFTX9-pfHsjb18TKoWACk";
    public static final String AA_PAGE16 = "1706556435";
    public static final String AA_PAGE_OTHERS;

    public static final String AB_SHEET = "1RnN6lE3yi5S_5PBuxMXdWNvN3HayP3054M3Qud_p9BU";
    public static final String AB_PAGE21 = "27712269";
    public static final String AB_PAGE20 = "1664598957";
    public static final String AB_PAGE19 = "1912774860";
    public static final String AB_PAGE18 = "1706556435";
    public static final String AB_PAGE16 = "1572184167";
    public static final String AB_PAGE_CHALLENGES;

    public static final String SUPPORTER_SHEET;
    public static final String NICKNAME_SHEET;
    public static final String PRIMARY_AA_HISTORY;

    public static final String ANY_RSG_RECORD;
    public static final String ANY_SSG_RECORD;
    public static final String AA_SSG_RECORD;

    private Web() {}

    public static String getUuidUrl(String name) {
      return "https://api.mojang.com/users/profiles/minecraft/" + name;
    }

    public static String getNameUrl(String uuid) {}

    public static String getAvatarUrlFallback(Uuid uuid, int size) {}

    public static String getAvatarUrl(Uuid uuid, int size) {}

    public static String getAvatarUrl(String name, int size) {}

    public static String getSpreadsheetUrl(String sheet, String page) {}

    public static String getSpeedrunDotComProfileUrl(String id) {}

    public static String getSpeedrunDotComPictureUrl(String id) {}

    public static String getAnyPercentRecordUrl(boolean rsg) {}
  }
}
