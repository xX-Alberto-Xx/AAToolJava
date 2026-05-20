package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class GoldBlocks extends ComplexObjective {
  public static final String ITEM_ID;
  public static final String LEGACY_ITEM_ID;
  public static final String GOLD_INGOT_ID;
  public static final int INGOTS_PER_BLOCK;

  public static final int REQUIRED;

  private static final String BEACONATOR;
  private static final String LEGACY_BEACONATOR;

  private static final Version BLOCK_ID_CHANGED;
  private static final Version TEXTURE_CHANGED;

  private boolean fullBeaconComplete;
  private int estimatedBlocks;

  private static boolean useModernId() {}
  private static boolean useModernTexture() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  public static int getPreciseEstimate(ProgressState progress) {}

  private void updatePreciseGoldEstimate(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getCurrentIcon() {}
}
