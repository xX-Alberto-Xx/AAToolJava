package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class WaxOnOff extends ComplexObjective {
  private static final String WAX_ON = "minecraft:husbandry/wax_on";
  private static final String WAX_OFF = "minecraft:husbandry/wax_off";

  private boolean waxOnComplete;
  private boolean waxOffComplete;

  public WaxOnOff() { this.icon = "wax_on"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.waxOnComplete = progress.advancementCompleted(WAX_ON);
    this.waxOffComplete = progress.advancementCompleted(WAX_OFF);
    this.completionOverride = this.waxOnComplete && this.waxOffComplete;
  }

  @Override
  protected void clearAdvancedState() {
    this.waxOnComplete = false;
    this.waxOffComplete = false;
  }

  @Override
  protected String getLongStatus() { return this.getShortStatus(); }

  @Override
  protected String getShortStatus() {
    return this.waxOnComplete && !this.waxOffComplete ? "Wax Off"
         : this.waxOffComplete && !this.waxOnComplete ? "Wax On"
         : "Wax On+Off";
  }

  @Override
  protected String getCurrentIcon() {
    return this.waxOnComplete && !waxOffComplete ? "wax_off" : "wax_on";
  }
}
