package org.mcsr.aatool.data.categories;

import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.Objective;

public class AllAchievements extends Category {
  public static final Set<String> SUPPORTED_VERSIONS = Set.of("1.11");

  public AllAchievements() {
    this.name = "All Achievements";
    this.acronym = "AACH";
    this.objective = "Achievements";
    this.action = "Complete";
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }

  @Override
  public Iterable<? extends Objective> getOverlayObjectives() {
    return Tracker.ACHIEVEMENTS.allAdvancements.values();
  }

  @Override
  public int getTargetCount() { return Tracker.ACHIEVEMENTS.getCount(); }
  @Override
  public int getCompletedCount() { return Tracker.ACHIEVEMENTS.getCombinedCompletedCount(); }

  @Override
  public void loadObjectives() {
    Tracker.ACHIEVEMENTS.refreshObjectives();
    Tracker.COMPLEX_OBJECTIVES.refreshObjectives();
  }
}
