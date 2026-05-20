package org.mcsr.aatool.data.categories;

import java.util.List;

public class AllSmithingTemplates extends SingleAdvancement {
  public static final List<String> SUPPORTED_VERSIONS;

  private static final String ID;

  private int recipesObtained;

  public AllSmithingTemplates() {}

  public final int getRecipesObtained() { return this.recipesObtained; }

  @Override
  public Iterable<String> getSupportedVersions() {}
  @Override
  public int getCompletedCount() {}

  @Override
  public void loadObjectives() {}

  @Override
  public void update() {}
}
