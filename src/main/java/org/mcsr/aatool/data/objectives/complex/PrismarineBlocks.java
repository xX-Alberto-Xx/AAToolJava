package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class PrismarineBlocks extends ComplexObjective {
  public static final String HDWGH_ID;
  public static final String CONDUIT;
  public static final String NORMAL;
  public static final String DARK;
  public static final String BRICKS;

  public static final int REQUIRED;

  private int total;
  private boolean hdwghComplete;
  private boolean conduitPlaced;

  public PrismarineBlocks() {}

  private boolean hasEnoughForConduit() {}

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
