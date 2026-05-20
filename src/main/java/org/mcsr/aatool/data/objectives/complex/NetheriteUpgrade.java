package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class NetheriteUpgrade extends ComplexObjective {
  public static final String RECIPE;

  private boolean obtained;

  public NetheriteUpgrade() {}

  public final boolean isObtained() { return this.obtained; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getLongStatus() {}
  @Override
  protected String getShortStatus() {}
}
