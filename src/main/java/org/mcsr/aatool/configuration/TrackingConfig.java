package org.mcsr.aatool.configuration;

import org.mcsr.aatool.enums.ProgressFilter;
import org.mcsr.aatool.enums.TrackerSource;
import org.mcsr.aatool.net.Uuid;

public class TrackingConfig extends Config {
  public final Setting<String> lastSession;
  public final Setting<String> lastPlayer;
  public final Setting<Uuid> lastUuid;
  public final Setting<String> currentRunnerProfileId;
  public final Setting<String> currentRunnerProfileName;

  public final Setting<String> gameCategory;
  public final Setting<String> gameVersion;

  public final Setting<Boolean> autoDetectVersion;
  public final Setting<Boolean> useSftp;

  public final Setting<TrackerSource> source;
  public final Setting<String> customWorldPath;
  public final Setting<String> customSavesPath;
  public final Setting<Boolean> manualChecklistMode;

  public final Setting<ProgressFilter> filter;
  public final Setting<String> soloFilterName;

  public final Setting<Boolean> broadcastProgress;
  public final Setting<String> openTrackerKey;
  public final Setting<String> openTrackerUrl;

  public final Setting<DateTime> lastOpenedAllBlocks;

  public TrackingConfig() {}

  public final String getCurrentRunnerProfileNameOrId() {}

  public final boolean isWatchingActiveInstance() {}

  public final boolean isSourceChanged() {}

  public final boolean isFilterChanged() {}

  @Override
  protected String getId() {}
}
