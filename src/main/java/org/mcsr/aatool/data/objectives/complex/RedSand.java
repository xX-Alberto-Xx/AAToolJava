package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class RedSand extends ComplexObjective {
  public static final String BLOCK_ID = "minecraft:red_sand";

  private boolean obtained;
  private boolean placed;

  public RedSand() { this.icon = "red_sand"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.obtained = progress.wasPickedUp(BLOCK_ID);
    this.placed = progress.wasUsed(BLOCK_ID);
    this.completionOverride = this.obtained || this.placed;
  }

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() { return "Red\0Sand"; }

  @Override
  protected String getLongStatus() {
    return this.placed ? "Red\0Sand\nPlaced"
         : this.obtained ? "Obtained\nRed\0Sand"
         : "Obtain\nRed\0Sand";
  }
}
