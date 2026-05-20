package org.mcsr.aatool.configuration;

import java.util.List;
import java.util.Map;

public abstract class Config {
  private static TrackingConfig tracking;
  private static MainConfig main;
  private static OverlayConfig overlay;
  private static NetworkConfig net;
  private static SftpConfig sftp;
  private static NotesConfig notes;

  private static final List<Config> ALL;

  private static final Map<Class, String> FILE_NAMES;

  private final List<SettingInterface<?>> settings;

  public static TrackingConfig getTracking() { return tracking; }
  public static MainConfig getMain() { return main; }
  public static OverlayConfig getOverlay() { return overlay; }
  public static NetworkConfig getNet() { return net; }
  public static SftpConfig getSftp() { return sftp; }
  public static NotesConfig getNotes() { return notes; }

  public static void initialize() {}

  public static void saveAll() {}

  public static void resetAllToDefaults() {}

  public static void clearAllFlags() {}

  public static boolean trySave(Config config) {}

  private static <T extends Config> void load() {}

  private static boolean tryParseLegacySetting(XmlNode setting, /*out */String key, /*out */Object value) {}

  private static void registerConfig(Config config) {}

  private static void archiveOldSettings() {}

  private String getFileName() {}
  private String getLegacyFileName() {}

  public final boolean trySave() {}

  public final void registerSetting(SettingInterface<?> setting) {}

  protected abstract String getId();

  protected void migrateDeprecatedConfigs() {}

  protected void applyDefaultValues() {}

  private void clearFlags() {}
}
