package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Trident extends ComplexObjective {
  private static final String VVF;
  private static final String SURGE;

  private static final Version PIGLIN_HEAD_ADDED;
  private static final Version ANCIENT_CITY_SKELETON_SKULLS;
  private static final Version SURGE_PROTECTOR_ADDED;
  private static final Version TRIDENT_FOUND_IN_VAULTS;

  private boolean obtained;

  private boolean vvfDone;
  private boolean surgeDone;
  private boolean ignoreSurge;

  private boolean piglinHead;
  private boolean zombieHead;
  private boolean creeperHead;
  private boolean skeletonSkull;

  private boolean doneWithHeads;

  public Trident() {}

  public final boolean hasEnchantedForegroundLayer() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getLongStatus() {}

  private String getStatusAA() {}

  @Override
  protected String getShortStatus() {}

  private String getStatusAB() {}

  @Override
  protected String getCurrentIcon() {}
}
