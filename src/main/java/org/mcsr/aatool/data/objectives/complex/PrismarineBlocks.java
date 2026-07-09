package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class PrismarineBlocks extends ComplexObjective {
  public static final String HDWGH_ID = "minecraft:nether/all_effects";
  public static final String CONDUIT = "minecraft:conduit";
  public static final String NORMAL = "minecraft:prismarine";
  public static final String DARK = "minecraft:dark_prismarine";
  public static final String BRICKS = "minecraft:prismarine_bricks";

  public static final int REQUIRED = 16;

  private int total;
  private boolean hdwghComplete;
  private boolean conduitPlaced;

  public PrismarineBlocks() { this.icon = "all_prismarine_blocks"; }

  private boolean hasEnoughForConduit() { return this.total >= REQUIRED; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.hdwghComplete = progress.advancementCompleted(HDWGH_ID);

    this.total = Math.max(
      getCount(NORMAL, progress) +
      getCount(DARK, progress) +
      getCount(BRICKS, progress),
      0
    );

    this.conduitPlaced = progress.timesUsed(CONDUIT) > 0;
    this.completionOverride = this.hdwghComplete || this.hasEnoughForConduit() || this.conduitPlaced;
  }

  private static int getCount(String id, ProgressState progress) {
    return progress.timesPickedUp(id)
         + progress.timesCrafted(id)
         - progress.timesDropped(id)
         - progress.timesUsed(id);
  }

  @Override
  protected String getCurrentIcon() { return "all_prismarine_blocks"; }

  @Override
  protected void clearAdvancedState() { this.total = 0; }

  @Override
  protected String getShortStatus() {
    return this.hdwghComplete || this.conduitPlaced ? ""
         : this.manuallyChecked ? "Collected"
         : this.total + "\0/\0" + REQUIRED;
  }

  @Override
  protected String getLongStatus() {
    return this.hdwghComplete
           ? "HDWGH\nCompleted"
           : "Prismarine\n" + this.total + "\0/\0" + REQUIRED;
  }
}
