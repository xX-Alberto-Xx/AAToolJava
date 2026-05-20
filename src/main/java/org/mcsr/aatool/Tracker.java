package org.mcsr.aatool;

import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.data.categories.Category;
import org.mcsr.aatool.data.objectives.AchievementManifest;
import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.objectives.AdvancementManifest;
import org.mcsr.aatool.data.objectives.Block;
import org.mcsr.aatool.data.objectives.BlockManifest;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.objectives.ComplexObjectiveManifest;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.Death;
import org.mcsr.aatool.data.objectives.DeathManifest;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.enums.TrackerSource;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.saves.WorldFolder;
import org.mcsr.aatool.utilities.Timer;
import org.mcsr.aatool.utils.Pair;

public final class Tracker {
  public static final AdvancementManifest ADVANCEMENTS;
  public static final AchievementManifest ACHIEVEMENTS;
  public static final ComplexObjectiveManifest COMPLEX_OBJECTIVES;
  public static final BlockManifest BLOCKS;
  public static final DeathManifest DEATHS;

  private static Category category;
  private static WorldState state;
  private static Exception lastError;
  private static boolean designationsChanged;
  private static boolean mainPlayerChanged;
  private static boolean coOpStateChanged;
  private static boolean worldLocked;
  private static String status;

  public static boolean manualChecklistInvalidated;
  public static boolean manualChecklistChanged;

  private static final FileSystemWatcher WORLD_WATCHER;
  private static final FileSystemWatcher PRACTICE_WORLD_WATCHER;
  private static final FileSystemWatcher ADVANCEMENTS_WATCHER;
  private static final FileSystemWatcher STATISTICS_WATCHER;

  private static WorldFolder world;
  private static Timer refreshTimer;
  private static Timer filesystemEventDebounce;
  private static TimeSpan lastInGameTime;
  private static Uuid previousMainPlayer;
  private static String previousSavesPath;
  private static String previousWorldPath;
  private static String lastServerMessage;
  private static int previousActiveId;
  private static boolean fileSystemEventRaised;
  private static double lastRefresh;

  private Tracker() {}

  public static Category getCategory() { return category; }
  public static WorldState getState() { return state; }
  public static Exception getLastError() { return lastError; }
  public static boolean areDesignationsChanged() { return designationsChanged; }
  public static boolean isMainPlayerChanged() { return mainPlayerChanged; }
  public static boolean isCoOpStateChanged() { return coOpStateChanged; }
  public static boolean isWorldLocked() { return worldLocked; }
  public static String getStatus() { return status; }

  public static boolean isInvalidated() {}

  public static boolean isSavesFolderChanged() {}

  public static boolean isWorldChanged() {}

  public static boolean areObjectivesChanged() {}

  public static boolean isProgressChanged() {}

  public static boolean isWorking() {}
  public static String getCurrentCategory() {}
  public static String getCurrentVersion() {}

  public static AdvancementManifest getCurrentAdvancementSet() {}

  public static Map<Pair<String, String>, Criterion> getAllCriteria() {}

  public static Map<Pair<String, String>, Criterion> getRemainingCriteria() {}

  public static String getWorldName() {}
  public static TimeSpan getInGameTime() {}
  public static boolean isInGameTimeChanged() {}
  public static TrackerSource getSource() {}

  public static void toggleWorldLock() {}

  public static String getFullIgt() {}

  public static String getShortIgt() {}

  public static String getDays() {}

  public static String getDaysAndHours() {}

  public static String getEstimateString(int seconds) {}

  public static String getLastRefresh(Time time) {}

  public static void invalidateDesignations() {}

  public static boolean tryGetAdvancement(String id, /*out */Advancement advancement) {}

  public static boolean tryGetCriterion(String adv, String crit, /*out */Criterion criterion) {}

  public static boolean tryGetAdvancementGroup(String id, /*out */Set<Advancement> group) {}

  public static boolean tryGetComplexObjective(String typeName, /*out */ComplexObjective item) {}

  public static boolean tryGetBlock(String id, /*out */Block block) {}

  public static boolean tryGetDeath(String id, /*out */Death death) {}

  public static Uuid getMainPlayer() {}

  public static Set<Uuid> getAllPlayers() {}

  public static void initialize() {}

  public static void fileSystemChanged(Object sender, FileSystemEventArgs e) {}

  public static String getStatusText() {}

  public static boolean trySetCategory(String category) {}

  public static boolean trySetVersion(String versionNumber) {}

  public static void invalidate(boolean invalidateWorld/* = false*/) {}

  public static void clearFlags() {}

  public static void update(Time time) {}

  private static void updateFileSystemWatchers() {}

  private static void updateCurrentWorld() {}

  private static void parseCoOpProgress(Time time, Client client) {}

  private static void readLocalFiles(Time time) {}

  private static void readManualChecklist(Time time) {}

  private static void readLatestLog() {}

  private static void setState(WorldState world) {}
}
