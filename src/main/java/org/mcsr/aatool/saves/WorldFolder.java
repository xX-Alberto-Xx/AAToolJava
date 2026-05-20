package org.mcsr.aatool.saves;

import java.util.Set;

import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.net.Uuid;

public class WorldFolder {
  private AdvancementsFolder advancements;
  private AchievementsFolder achievements;
  private StatisticsFolder statistics;
  private DirectoryInfo currentFolder;
  private boolean progressChanged;
  private boolean invalidated;
  private boolean pathChanged;

  public WorldFolder() {}

  public final AdvancementsFolder getAdvancements() { return this.advancements; }
  public final AchievementsFolder getAchievements() { return this.achievements; }
  public final StatisticsFolder getStatistics() { return this.statistics; }
  public final DirectoryInfo getCurrentFolder() { return this.currentFolder; }
  public final boolean isProgressChanged() { return this.progressChanged; }
  public final boolean isInvalidated() { return this.invalidated; }
  public final boolean isPathChanged() { return this.pathChanged; }

  public final boolean isEmpty() {}

  public final String getFullName() {}
  public final String getName() {}

  public final Set<Uuid> getAllUuids() {}

  public final void invalidate() {}

  public final void clearFlags() {}

  public final void unset() {}

  public final void setPath(DirectoryInfo worldFolder) {}

  public final boolean tryRefresh() {}

  public final WorldState getState() {}
}
