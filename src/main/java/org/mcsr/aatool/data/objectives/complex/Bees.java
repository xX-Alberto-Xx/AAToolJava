package org.mcsr.aatool.data.objectives.complex;

import java.util.List;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Bees extends ComplexObjective {
  public static final String BLOCK_ID;

  private static final String TOTAL_BEELOCATION;
  private static final String BEE_OUR_GUEST;
  private static final String BALANCED_DIET;
  private static final String TWO_BY_TWO;
  private static final String STICKY_SITUATION;
  private static final String WAX_ON;
  private static final String WAX_OFF;
  private static final String HONEY_BOTTLE;
  private static final String BEE;

  private static final String HONEY_BLOCK;
  private static final String HONEY_COMB_BLOCK;

  private static final String EMPTY_TEXTURE;
  private static final String FULL_TEXTURE;

  private static final Version CAVES_AND_CLIFFS_PART_ONE;

  private int estimatedCount;
  private int estimatedPlaced;

  private boolean totalBeelocation;
  private boolean beeOurGuest;
  private boolean stickySituation;
  private boolean waxOn;
  private boolean waxOff;
  private boolean drinkHoney;
  private boolean breedBees;

  private boolean balancedDiet;
  private boolean twoByTwo;

  private boolean honeyBlockPlaced;
  private boolean honeycombBlockPlaced;
  private boolean allCandlesPlaced;
  private boolean allWaxedCopperPlaced;

  private final List<String> remainingObjectives;
  private boolean doneWithBees;

  public static final String[] ALL_WAXED_COPPER;

  public static final String[] ALL_CANDLES;

  public Bees() {}

  private boolean isCopperAndCandlesAdded() {}

  private boolean isWaxAdvancementsAdded() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  private void buildRemainingObjectiveList() {}

  private boolean allWaxedCopperPlaced(ProgressState progress) {}

  private boolean allCandlesPlaced(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getCurrentIcon() {}
}
