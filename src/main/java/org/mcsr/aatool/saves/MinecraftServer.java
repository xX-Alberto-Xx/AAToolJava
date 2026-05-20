package org.mcsr.aatool.saves;

import java.util.List;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.enums.SyncState;
import org.mcsr.aatool.utilities.Timer;

public final class MinecraftServer {
  private static final String SFTP_PREFIX;
  private static final double RETRY_INTERVAL;
  private static final int MAXIMUM_RETRIES;
  private static final int ATTEMPT_INTERVAL_MS;

  private static Exception lastError;
  private static DateTime lastWorldSave;
  private static SyncState state;
  private static String messageOfTheDay;
  private static String worldName;

  private static final Timer REFRESH_TIMER;

  private static ConnectionInfo credentials;
  private static int currentDownloadPercent;
  private static float smoothDownloadPercent;
  private static boolean linuxMode;

  private MinecraftServer() {}

  public static Exception getLastError() { return lastError; }
  public static DateTime getLastWorldSave() { return lastWorldSave; }
  public static SyncState getState() { return state; }
  public static String getMessageOfTheDay() { return messageOfTheDay; }
  public static String getWorldName() { return worldName; }

  public static double getSaveInterval() {}
  public static boolean isCredentialsValidated() {}
  public static boolean isLastSyncFailed() {}
  public static boolean isDownloading() {}
  public static boolean isEnabled() {}

  public static int getNextRefresh() {}
  public static void invalidateWorld() {}
  private static void setState(SyncState state) {}

  public static DateTime getRefreshEstimate() {}

  public static String hostAwarePath(/*params */String[] paths) {}

  public static void update(Time time) {}

  public static void sync() {}

  public static String getLongStatusText() {}

  public static String getShortStatusText() {}

  private static void clearCredentials() {}

  private static void applyCredentials() {}

  private static boolean tryConnect(/*out */SftpClient sftp) {}

  private static boolean tryDownloadProgress(SftpClient sftp) {}

  private static boolean tryGetProperty(String[] properties, String key, /*out */String value) {}

  private static boolean tryDownloadServerProperties(SftpClient sftp, int failures/* = 0*/) {}

  private static boolean tryGetWorldSaveTime(SftpClient sftp, /*out */DateTime lastWorldSave, int failures/* = 0*/) {}

  public static boolean tryDownloadFolder(SftpClient sftp, String name, int failures/* = 0*/) {}

  private static boolean tryDownloadFile(SftpClient sftp, String remote, String local, int failures/* = 0*/) {}

  private static void deleteDeprecatedFiles(List<SftpFile> source, List<FileInfo> destination) {}

  private static void downloadAll(SftpClient sftp, Iterable<SftpFile> files, String downloadFolder) {}

  private static List<FileInfo> getFiles(String directory) {}
}
