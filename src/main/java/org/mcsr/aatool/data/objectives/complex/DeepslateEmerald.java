package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class DeepslateEmerald extends ComplexObjective {
  public static final String BLOCK_ID = "minecraft:deepslate_emerald_ore";

  private boolean obtained;
  private boolean placed;

  public DeepslateEmerald() { this.icon = "deepslate_emerald_ore"; }

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
  protected String getShortStatus() { return "Deepslate Emerald"; }

  @Override
  protected String getLongStatus() {
    return this.placed ? "DS\0Emerald\nPlaced"
         : this.obtained ? "DS\0Emerald\nObtained"
         : "Deepslate\nEmerald";
  }
}
