package org.mcsr.aatool.configuration;

import java.nio.file.Path;
import java.time.Instant;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllDeaths;
import org.mcsr.aatool.enums.ProgressFilter;
import org.mcsr.aatool.enums.TrackerSource;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.Strings;

public class TrackingConfig extends Config {
  public final Setting<String> lastSession = new Setting<>("");
  public final Setting<String> lastPlayer = new Setting<>("");
  public final Setting<Uuid> lastUuid = new Setting<>(Uuid.EMPTY);
  public final Setting<String> currentRunnerProfileId = new Setting<>("");
  public final Setting<String> currentRunnerProfileName = new Setting<>("");

  public final Setting<String> gameCategory = new Setting<>("All Advancements");
  public final Setting<String> gameVersion = new Setting<>("1.16");

  public final Setting<Boolean> autoDetectVersion = new Setting<>(true);
  public final Setting<Boolean> useSftp = new Setting<>(false);

  public final Setting<TrackerSource> source = new Setting<>(TrackerSource.ACTIVE_INSTANCE);
  public final Setting<Path> customWorldPath = new Setting<>(Paths.EMPTY_PATH);
  public final Setting<Path> customSavesPath = new Setting<>(Paths.Saves.MINECRAFT.resolve("saves"));
  public final Setting<Boolean> manualChecklistMode = new Setting<>(false);

  public final Setting<ProgressFilter> filter = new Setting<>(ProgressFilter.COMBINED);
  public final Setting<String> soloFilterName = new Setting<>("");

  public final Setting<Boolean> broadcastProgress = new Setting<>(false);
  public final Setting<String> openTrackerKey = new Setting<>("");
  public final Setting<String> openTrackerUrl = new Setting<>("");

  public final Setting<Instant> lastOpenedAllBlocks = new Setting<>(Instant.MIN);

  public TrackingConfig() {
    this.registerSetting(this.lastSession);

    this.registerSetting(this.gameCategory);
    this.registerSetting(this.gameVersion);

    this.registerSetting(this.autoDetectVersion);
    this.registerSetting(this.useSftp);

    this.registerSetting(this.source);
    this.registerSetting(this.customSavesPath);
    this.registerSetting(this.customWorldPath);

    this.registerSetting(this.filter);
    this.registerSetting(this.soloFilterName);

    this.registerSetting(this.broadcastProgress);
    this.registerSetting(this.openTrackerKey);
    this.registerSetting(this.openTrackerUrl);

    this.registerSetting(this.lastOpenedAllBlocks);
  }

  public final String getCurrentRunnerProfileNameOrId() {
    return (
      !Strings.isNullOrEmpty(this.currentRunnerProfileId.getValue())
      ? this.currentRunnerProfileId
      : this.currentRunnerProfileName
    ).getValue();
  }

  public final boolean isWatchingActiveInstance() {
    return !this.useSftp.getValue() && (
      this.source.getValue() == TrackerSource.ACTIVE_INSTANCE ||
      this.autoDetectVersion.getValue() ||
      Tracker.getCategory() instanceof AllDeaths
    );
  }

  public final boolean isSourceChanged() {
    return this.source.isChanged()
        || this.useSftp.isChanged()
        || this.isFilterChanged()
        || this.manualChecklistMode.isChanged()
        || (this.source.getValue() == TrackerSource.CUSTOM_SAVES_PATH && this.customSavesPath.isChanged())
        || (this.source.getValue() == TrackerSource.SPECIFIC_WORLD && this.customWorldPath.isChanged());
  }

  public final boolean isFilterChanged() {
    return this.filter.isChanged() || (
      this.filter.getValue() == ProgressFilter.SOLO && this.soloFilterName.isChanged()
    );
  }

  @Override
  protected String getId() { return "tracking"; }
}
