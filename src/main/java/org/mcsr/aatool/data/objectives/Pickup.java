package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;

public class Pickup extends ComplexObjective {
  protected int obtained;
  private final int required;

  public Pickup(String id, String name/* = null*/, int required/* = 1*/) {}

  public final int getObtained() { return this.obtained; }
  public int getRequired() { return this.required; }

  @Override
  public String getFullStatus() {}
  @Override
  public String getTinyStatus() {}

  protected int getCount(ProgressState progress) {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}
}
