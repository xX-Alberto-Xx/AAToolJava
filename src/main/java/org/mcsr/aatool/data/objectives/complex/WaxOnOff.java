package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class WaxOnOff extends ComplexObjective {
  private static final String WAX_ON;
  private static final String WAX_OFF;

  public boolean waxOnComplete;
  public boolean waxOffComplete;

  public WaxOnOff() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getCurrentIcon() {}
}
