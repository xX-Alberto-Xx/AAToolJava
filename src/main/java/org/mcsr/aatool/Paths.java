package org.mcsr.aatool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Stream;

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
    public static final Path CONFIG_FOLDER = Path.of("config");
    public static final String LEGACY_SETTINGS_FOLDER;
    public static final String ARCHIVED_CONFIG_FOLDER;
    public static final String NOTES_FOLDER;

    public static final String CACHE_FOLDER;
    public static final String LEADERBOARDS_FOLDER;
    public static final String BLOCK_CHECKLISTS_FOLDER;
    public static final String PROFILE_PICTURES_CACHE_FOLDER;
    public static final String PROFILE_DETAILS_CACHE_FOLDER;
    public static final String SFTP_WORLDS_FOLDER;

    public static final String MANUAL_CHECKLIST_FOLDER;

    public static final String DATA_FOLDER;
    public static final String LOGS_FOLDER;
    public static final String ASSETS_FOLDER;
    public static final String OBJECTIVES_FOLDER;
    public static final String VIEWS_FOLDER;
    public static final String TEMPLATES_FOLDER;
    public static final String SPRITES_FOLDER;
    public static final String FONTS_FOLDER;
    public static final String AVATAR_CACHE_FOLDER;
    public static final String CREDITS_FOLDER;
    public static final String WINFORMS_ASSETS;

    public static final String MAIN_ICON;
    public static final String UPDATE_ICON;

    public static final String UPDATE_EXECUTABLE;

    private System() {}

    public static String getObjectiveFolder() {}
    public static String getAdvancementsFolder() {}
    public static String getAchievementsFile() {}
    public static String getDeathMessagesFile() {}
    public static String getArmorTrimsFile() {}
    public static String getPotionsFile() {}

    public static String getCrashLogFile() {}
    public static String getCreditsFile() {}

    public static String getHistoryFile() {}

    public static String getChallengesFile() {}

    public static String getSupportersFile() {}

    public static String leaderboardFile(String fileName) {}

    public static String blockChecklistFile(int instance, String worldName) {}

    public static String speedrunDotComLeaderboardFile(String category, String version) {}

    public static String speedrunDotComRecordFile(boolean rsg, boolean aa, String version) {}

    public static String speedrunDotComProfilePicture(String id) {}

    public static String speedrunDotComProfileJson(String idOrName) {}
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

    public static final String AA_SHEET;
    public static final String AA_PAGE16;
    public static final String AA_PAGE_OTHERS;

    public static final String AB_SHEET;
    public static final String AB_PAGE21;
    public static final String AB_PAGE20;
    public static final String AB_PAGE19;
    public static final String AB_PAGE18;
    public static final String AB_PAGE16;
    public static final String AB_PAGE_CHALLENGES;

    public static final String SUPPORTER_SHEET;
    public static final String NICKNAME_SHEET;
    public static final String PRIMARY_AA_HISTORY;

    public static final String ANY_RSG_RECORD;
    public static final String ANY_SSG_RECORD;
    public static final String AA_SSG_RECORD;

    private Web() {}

    public static String getUuidUrl(String name) {}

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
