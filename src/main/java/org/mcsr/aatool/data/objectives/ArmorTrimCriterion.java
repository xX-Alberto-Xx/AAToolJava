package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;

public class ArmorTrimCriterion extends Criterion {
  private String recipe;
  private boolean obtained;

  private String plainName;

  public ArmorTrimCriterion(XmlNode node, Advancement advancement) {}

  public final String getRecipe() { return this.recipe; }
  public final boolean isObtained() { return this.obtained; }
  public final boolean isApplied() {}

  @Override
  public boolean completedByDesignated() {}
  @Override
  public boolean isComplete() {}

  @Override
  public void updateState(ProgressState progress) {}
}
