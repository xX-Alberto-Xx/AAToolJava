package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class AncientDebris extends ComplexObjective {
  public static final String ANCIENT_DEBRIS_ID;
  public static final String NETHERITE_SCRAP_ID;
  public static final String NETHERITE_INGOT_ID;

  private static final String NETHERITE_BLOCK;
  private static final String OBTAIN_DEBRIS;
  private static final String USE_LODESTONE;
  private static final String NETHERITE_HOE;
  private static final String NETHERITE_ARMOR;
  private static final String TNT;

  public static final int REQUIRED;

  protected boolean completedHiddenInTheDepths;
  protected boolean completedCountryLode;
  protected boolean completedSeriousDedication;
  protected boolean completedCoverMeInDebris;

  protected boolean craftedNetheriteBlock;
  protected boolean placedNetheriteBlock;

  private boolean allNetheriteAdvancementsComplete;
  private int estimatedDebris;
  private int estimatedTnt;

  public final boolean areAllNetheriteAdvancementsComplete() { return this.allNetheriteAdvancementsComplete; }
  public final int getEstimatedDebris() { return this.estimatedDebris; }
  public final int getEstimatedTnt() { return this.estimatedTnt; }

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
