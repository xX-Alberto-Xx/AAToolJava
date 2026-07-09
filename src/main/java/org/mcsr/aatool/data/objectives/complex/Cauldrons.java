package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class Cauldrons extends Pickup {
  public static final String ITEM_ID = "minecraft:cauldron";
  public static final String LIGHT_AS_A_RABBIT = "minecraft:adventure/walk_on_powder_snow_with_leather_boots";

  private boolean advancementComplete;
  private int placed;

  public Cauldrons() { super(ITEM_ID); }

  @Override
  public int getRequired() { return Integer.MAX_VALUE; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.placed = progress.timesUsed(ITEM_ID);
    this.advancementComplete = progress.advancementCompleted(LIGHT_AS_A_RABBIT);
    super.updateAdvancedState(progress);
    this.completionOverride |= this.advancementComplete;
  }

  @Override
  protected void clearAdvancedState() {
    super.clearAdvancedState();
    this.advancementComplete = false;
    this.placed = 0;
  }

  @Override
  protected String getShortStatus() {
    return this.advancementComplete ? "Done" : "Placed:\0" + this.placed;
  }

  @Override
  protected String getLongStatus() {
    return this.advancementComplete ? "LaaR\nComplete" : "Cauldrons\nPlaced:\0" + this.placed;
  }

  @Override
  protected String getCurrentIcon() { return "cauldron"; }
}
