package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class NautilusShells extends Pickup {
  private static final String HDWGH;
  private static final String CONDUIT;

  private boolean conduitCrafted;
  private boolean conduitPlaced;
  private boolean hdwghComplete;

  public NautilusShells() {}

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
