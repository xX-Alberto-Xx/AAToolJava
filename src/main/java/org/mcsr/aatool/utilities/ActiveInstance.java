package org.mcsr.aatool.utilities;

import java.nio.file.Path;

import org.mcsr.aatool.Time;

public final class ActiveInstance {
  private static final String INSTANCE_NUMBER_FILE_NAME;
  private static final String GAME_DIR_FLAG;
  private static final String NATIVES_FLAG;

  private static String dotMinecraftPath;
  private static Path savesPath;
  private static Path practiceSavesPath;
  private static String logFile;
  private static int number;
  private static int lastActiveId;

  private static final Timer REFRESH_COOLDOWN;

  private static String latestLogContents;
  private static String latestGameVersion;
  private static DateTime lastLogWriteTimeUtc;
  private static int logStart;

  private ActiveInstance() {}

  public static String getDotMinecraftPath() { return dotMinecraftPath; }
  public static Path getSavesPath() { return savesPath; }
  public static Path getPracticeSavesPath() { return practiceSavesPath; }
  public static String getLogFile() { return logFile; }
  public static int getNumber() { return number; }
  public static int getLastActiveId() { return lastActiveId; }

  public static boolean hasNumber() {}
  public static boolean isWatching() {}

  public static void setLogStart() {}

  public static void update(Time time) {}

  private static String commandLine(Process process) {}

  private static boolean tryGetActive(/*out */Process instance) {}

  private static boolean tryParseDotMinecraft(String args, /*out */DirectoryInfo folder) {}

  public static boolean tryGetLog(/*out */String latestLog) {}

  private static void updateInstanceNumber(String dotMinecraft) {}

  private static void updateGameVersion(Process instance) {}
}
