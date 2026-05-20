package org.mcsr.aatool.data.objectives.complex;

import java.util.List;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Sniffers extends ComplexObjective {
  public static final String ITEM_ID;
  private static final String SNIFFER;
  private static final String OBTAIN_EGG;
  private static final String TWO_BY_TWO;
  private static final String LITTLE_SNIFFS;
  private static final String PLANTING_THE_PAST;

  private int estimatedObtained;
  private int estimatedPlaced;

  private boolean eggObtained;
  private boolean sniffersBred;
  private boolean sniffletFed;
  private boolean seedPlanted;

  private boolean doneWithSniffers;

  private final List<String> remainingObjectives;

  public Sniffers() {}

  public final int getEstimatedObtained() { return this.estimatedObtained; }
  public final int getEstimatedPlaced() { return this.estimatedPlaced; }

  public final int getRequired() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  private void updateRemainingObjectives() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getCurrentIcon() {}
}
