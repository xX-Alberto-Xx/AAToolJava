package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class TridentAdvancements extends ComplexObjective {
  private static final String ATJ = "minecraft:adventure/throw_trident";
  private static final String VVF = "minecraft:adventure/very_very_frightening";

  private boolean atjComplete;
  private boolean vvfComplete;

  public TridentAdvancements() { this.icon = "trident"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.atjComplete = progress.advancementCompleted(ATJ);
    this.vvfComplete = progress.advancementCompleted(VVF);
    this.completionOverride = this.atjComplete && this.vvfComplete;
  }

  @Override
  protected void clearAdvancedState() {
    this.atjComplete = false;
    this.vvfComplete = false;
  }

  @Override
  protected String getLongStatus() { return this.getShortStatus(); }

  @Override
  protected String getShortStatus() {
    return this.atjComplete && !this.vvfComplete ? "VVF"
         : this.vvfComplete && !this.atjComplete ? "ATJ"
         : "ATJ + VVF";
  }
}
