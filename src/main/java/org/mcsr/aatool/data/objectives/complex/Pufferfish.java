package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class Pufferfish extends Pickup {
  public static final String HWDWGH_ID;
  public static final String BALANCED_DIET_ID;
  public static final String ITEM_ID;

  private boolean advancementsComplete;

  public Pufferfish() {}

  @Override
  public int getRequired() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getCurrentIcon() {}
}
