package org.mcsr.aatool.data.objectives.complex;

import java.util.Set;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class PotteryShards extends ComplexObjective {
  public static final String BRUSH_ADVANCEMENT;
  public static final String POT_ADVANCEMENT;

  public static final int REQUIRED;

  public final Set<String> all;

  private int obtained;

  private boolean advancementsComplete;

  public PotteryShards() {}

  public final int getObtained() { return this.obtained; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  private int getCount(String id, ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getCurrentIcon() {}
}
