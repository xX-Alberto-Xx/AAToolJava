package org.mcsr.aatool.configuration;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public abstract class Config {
  private static TrackingConfig tracking;
  private static MainConfig main;
  private static OverlayConfig overlay;
  private static NetworkConfig net;
  private static SftpConfig sftp;
  private static NotesConfig notes;

  private static final List<Config> ALL = new ArrayList<>();

  private static final Map<Class<? extends Config>, String> FILE_NAMES = Map.of(
    TrackingConfig.class, "config_tracking.json",
    MainConfig.class, "config_main.json",
    OverlayConfig.class, "config_overlay.json",
    NetworkConfig.class, "config_network.json",
    SftpConfig.class, "config_sftp.json",
    NotesConfig.class, "config_notes.json"
  );

  private final List<SettingInterface<?>> settings = new ArrayList<>();

  public static TrackingConfig getTracking() { return tracking; }
  public static MainConfig getMain() { return main; }
  public static OverlayConfig getOverlay() { return overlay; }
  public static NetworkConfig getNet() { return net; }
  public static SftpConfig getSftp() { return sftp; }
  public static NotesConfig getNotes() { return notes; }

  public static void initialize() {
    load(TrackingConfig.class);
    load(MainConfig.class);
    load(OverlayConfig.class);
    load(NetworkConfig.class);
    load(SftpConfig.class);
    load(NotesConfig.class);
  }

  public static void saveAll() {
    for (Config config : ALL) config.trySave();
  }

  public static void resetAllToDefaults() {
    for (Config config : ALL) config.applyDefaultValues();
    saveAll();
  }

  public static void clearAllFlags() {
    for (Config config : ALL) config.clearFlags();
  }

  public static boolean trySave(Config config) {
    try {
      Files.createDirectories(Paths.System.CONFIG_FOLDER);
      Path file = Paths.System.CONFIG_FOLDER.resolve(config.getFileName());

      try (Writer writer = Files.newBufferedWriter(file)) {
        JsonUtils.STRICT_GSON.toJson(config, writer);
        return true;
      }
    } catch (IOException | JsonIOException ignored) {
      return false;
    }
  }

  private static <T extends Config> void load(Class<T> classOfT) {
    T config = null;

    try {
      Path file = Paths.System.CONFIG_FOLDER.resolve(FILE_NAMES.get(classOfT));

      try (Reader reader = Files.newBufferedReader(file)) {
        config = JsonUtils.STRICT_GSON.fromJson(reader, classOfT);
      }

      config.migrateDeprecatedConfigs();
    } catch (IOException | JsonSyntaxException | JsonIOException ignored) {
      try {
        config = classOfT.getConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
        throw new AssertionError(classOfT.getSimpleName() + " cannot be instantiated", e);
      }

      trySave(config);
    } finally {
      registerConfig(config);
    }
  }

  private static void registerConfig(Config config) {
    if (config instanceof TrackingConfig trackingConfig) tracking = trackingConfig;
    else if (config instanceof MainConfig mainConfig) main = mainConfig;
    else if (config instanceof OverlayConfig overlayConfig) overlay = overlayConfig;
    else if (config instanceof NetworkConfig networkConfig) net = networkConfig;
    else if (config instanceof SftpConfig sftpConfig) sftp = sftpConfig;
    else if (config instanceof NotesConfig notesConfig) notes = notesConfig;
    else return;

    ALL.add(config);
  }

  private String getFileName() { return "config_" + this.getId() + ".json"; }

  public final boolean trySave() { return trySave(this); }

  public final void registerSetting(SettingInterface<?> setting) {
    if (setting != null && !this.settings.contains(setting)) {
      this.settings.add(setting);
    }
  }

  protected abstract String getId();

  protected void migrateDeprecatedConfigs() {}

  protected void applyDefaultValues() {
    for (SettingInterface<?> setting : this.settings) setting.applyDefault();
  }

  private void clearFlags() {
    for (SettingInterface<?> setting : this.settings) setting.clearFlag();
  }
}
