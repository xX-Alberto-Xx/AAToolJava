package org.mcsr.aatool.data.categories;

import java.util.Set;

import org.mcsr.aatool.data.objectives.Objective;

public class AllAchievements extends Category {
  public static final Set<String> SUPPORTED_VERSIONS;

  public AllAchievements() {}

  @Override
  public Iterable<String> getSupportedVersions() {}
  @Override
  public Iterable<Objective> getOverlayObjectives() {}

  @Override
  public int getTargetCount() {}
  @Override
  public int getCompletedCount() {}

  @Override
  public void loadObjectives() {}
}
