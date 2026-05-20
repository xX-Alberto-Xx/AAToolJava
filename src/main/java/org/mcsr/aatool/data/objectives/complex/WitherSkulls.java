package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class WitherSkulls extends ComplexObjective {
  public static final String ITEM_ID;
  public static final String LEGACY_ITEM_ID;
  private static final String MONSTER_HUNTER;
  private static final String SUMMON_WITHER;
  private static final String WITHER;
  private static final String WITHER_SKELETON;
  private static final String WITHER_ROSE;
  private static final String BEACON;

  private static final String BEACONATOR;
  private static final String LEGACY_BEACONATOR;

  private static final Version BLOCK_ID_CHANGED;
  private static final Version WITHER_KILL_REQUIRED;

  private int estimatedObtained;

  private boolean fullBeaconComplete;

  private boolean rosePlaced;
  private boolean beaconPlaced;
  private boolean witherSummoned;
  private boolean witherKilled;
  private int witherSkeletonsKilled;

  public WitherSkulls() {}

  public final int getEstimatedObtained() { return this.estimatedObtained; }

  private static boolean useModernId() {}

  public final int getRequired() {}

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
