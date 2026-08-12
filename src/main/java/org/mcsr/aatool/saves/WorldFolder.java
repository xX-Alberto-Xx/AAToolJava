package org.mcsr.aatool.saves;

import java.nio.file.Path;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.data.objectives.Death;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.utilities.ActiveInstance;

public class WorldFolder {
  private AdvancementsFolder advancements = new AdvancementsFolder();
  private AchievementsFolder achievements = new AchievementsFolder();
  private StatisticsFolder statistics = new StatisticsFolder();
  private Path currentFolder;
  private boolean progressChanged = true;
  private boolean invalidated;
  private boolean pathChanged = true;

  public final AdvancementsFolder getAdvancements() { return this.advancements; }
  public final AchievementsFolder getAchievements() { return this.achievements; }
  public final StatisticsFolder getStatistics() { return this.statistics; }
  public final Path getCurrentFolder() { return this.currentFolder; }
  public final boolean isProgressChanged() { return this.progressChanged; }
  public final boolean isInvalidated() { return this.invalidated; }
  public final boolean isPathChanged() { return this.pathChanged; }

  public final boolean isEmpty() { return this.currentFolder == null; }

  public final Path getFullName() {
    return this.currentFolder != null ? this.currentFolder.toAbsolutePath() : Paths.EMPTY_PATH;
  }

  public final String getName() {
    return this.currentFolder != null ? this.currentFolder.getFileName().toString() : "";
  }

  public final void invalidate() { this.invalidated = true; }

  public final void clearFlags() {
    this.pathChanged = false;
    this.progressChanged = false;
  }

  public final void unset() { this.setPath(null); }

  public final void setPath(Path worldFolder) {
    if (worldFolder == null) {
      if (this.currentFolder != null) {
        // No world folder to track
        this.currentFolder = null;
        this.advancements.setPath(null);
        this.achievements.setPath(null);
        this.statistics.setPath(null);
      }

      return;
    }

    Path fullWorldFolder = worldFolder.toAbsolutePath();

    // Make sure folder actually changed
    if (this.currentFolder == null || !fullWorldFolder.equals(this.currentFolder.toAbsolutePath())) {
      ActiveInstance.setLogStart();
      for (Death death : Tracker.DEATHS.getAll().values()) death.clear();

      this.currentFolder = worldFolder;
      this.pathChanged = true;
      Path statisticsFolder = fullWorldFolder.resolve("stats");

      // World changed
      this.advancements.setPath(fullWorldFolder.resolve("advancements"));
      this.achievements.setPath(statisticsFolder);
      this.statistics.setPath(statisticsFolder);

      // Unlock world
      if (Tracker.isWorldLocked()) Tracker.toggleWorldLock();
    }
  }

  public final boolean tryRefresh() {
    // Update progress
    this.progressChanged = this.statistics.tryRefresh() | (
      Tracker.getCategory() instanceof AllAchievements ? this.achievements : this.advancements
    ).tryRefresh() || this.invalidated;

    this.invalidated = false;
    return this.progressChanged || this.pathChanged;
  }

  public final WorldState getState() {
    WorldState state = new WorldState();

    // Sync progress from local world
    (Tracker.getCategory() instanceof AllAchievements ? this.achievements : this.advancements)
      .update(state);

    // Sync statistics from local world
    this.statistics.update(state);
    return state;
  }
}
