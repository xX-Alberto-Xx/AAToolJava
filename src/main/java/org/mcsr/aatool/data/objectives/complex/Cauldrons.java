package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class Cauldrons extends Pickup {
  public static final String ITEM_ID;
  public static final String LIGHT_AS_A_RABBIT;

  private boolean advancementComplete;
  private int placed;

  public Cauldrons() {}

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
