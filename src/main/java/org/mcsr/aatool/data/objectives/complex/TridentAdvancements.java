package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class TridentAdvancements extends ComplexObjective {
  private static final String ATJ;
  private static final String VVF;

  public boolean atjComplete;
  public boolean vvfComplete;

  public TridentAdvancements() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getShortStatus() {}
}
