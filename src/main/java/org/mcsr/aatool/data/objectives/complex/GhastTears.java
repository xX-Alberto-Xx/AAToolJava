package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class GhastTears extends ComplexObjective {
  public static final String ADVANCEMENT_ID;
  public static final String TEAR_ID;
  public static final String CRYSTAL_ID;

  private int tears;
  private int crystals;
  private boolean dragonRespawned;

  public GhastTears() {}

  private boolean hasAllTears() {}
  private boolean hasAnyCrystals() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected String getCurrentIcon() {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}
}
