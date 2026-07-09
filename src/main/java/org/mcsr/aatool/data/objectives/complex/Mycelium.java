package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Mycelium extends ComplexObjective {
  public static final String BLOCK_ID = "minecraft:mycelium";

  private boolean obtained;
  private boolean placed;

  public Mycelium() { this.icon = "mycelium"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.obtained = progress.wasPickedUp(BLOCK_ID);
    this.placed = progress.wasUsed(BLOCK_ID);
    this.completionOverride = this.obtained || this.placed;
  }

  @Override
  protected void clearAdvancedState() {
    this.obtained = false;
    this.placed = false;
  }

  @Override
  protected String getShortStatus() {
    return this.placed ? "Placed" : this.obtained ? "Obtained" : "0";
  }

  @Override
  protected String getLongStatus() {
    return this.placed ? "Mycelium\nPlaced"
         : this.obtained ? "Mycelium\nObtained"
         : "Obtain\nMycelium";
  }
}
