package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class EnderPearls extends ComplexObjective {
  public static final String PEARL_ID = "minecraft:ender_pearl";
  public static final String EYE_ID = "minecraft:ender_eye";

  private int estimate;

  public EnderPearls() { this.icon = "ender_pearl"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.estimate = Math.max(
      progress.timesPickedUp(PEARL_ID) -
      progress.timesDropped(PEARL_ID) -
      progress.timesUsed(PEARL_ID) -
      progress.timesCrafted(EYE_ID),
      0
    );

    this.completionOverride = this.estimate > 0;
  }

  @Override
  protected void clearAdvancedState() { this.estimate = 0; }

  @Override
  protected String getShortStatus() { return this.getStatus(); }

  @Override
  protected String getLongStatus() { return this.getStatus(); }

  private String getStatus() { return this.estimate + "\0Pearls"; }
}
