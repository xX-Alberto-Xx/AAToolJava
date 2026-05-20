package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.data.objectives.Objective;

public class AllAdvancements extends Category {
  public static final List<String> SUPPORTED_VERSIONS;

  public AllAdvancements() {}

  @Override
  public Iterable<String> getSupportedVersions() {}
  @Override
  public Iterable<Objective> getOverlayObjectives() {}

  @Override
  public String getDefaultVersion() {}

  @Override
  public int getTargetCount() {}
  @Override
  public int getCompletedCount() {}

  @Override
  public void loadObjectives() {}
}
