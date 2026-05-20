package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.data.objectives.Objective;

public class AllDeaths extends Category {
  public static final List<String> SUPPORTED_VERSIONS;

  public AllDeaths() {}

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
