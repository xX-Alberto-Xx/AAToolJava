package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class HeavyCore extends ComplexObjective {
  public static final String HEAVY_CORE_ID = "minecraft:heavy_core";
  public static final String MACE_ID = "minecraft:mace";
  public static final String OMINOUS_TRIAL_KEY_ID = "minecraft:ominous_trial_key";
  public static final String OVER_OVERKILL_ADVANCEMENT = "minecraft:adventure/overoverkill";

  private boolean obtainedHeavyCore;
  private boolean placedHeavyCore;
  private boolean maceCrafted;
  private boolean overOverkillComplete;
  private int ominousVaultsOpened;

  public final boolean obtainedHeavyCore() { return this.obtainedHeavyCore; }
  public final boolean placedHeavyCore() { return this.placedHeavyCore; }
  public final boolean isMaceCrafted() { return this.maceCrafted; }
  public final boolean isOverOverkillComplete() { return this.overOverkillComplete; }
  public final int getOminousVaultsOpened() { return this.ominousVaultsOpened; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.obtainedHeavyCore = progress.wasPickedUp(HEAVY_CORE_ID);
    this.placedHeavyCore = progress.wasUsed(HEAVY_CORE_ID);
    this.maceCrafted = progress.wasCrafted(MACE_ID);
    this.overOverkillComplete = progress.advancementCompleted(OVER_OVERKILL_ADVANCEMENT);
    this.ominousVaultsOpened = progress.timesUsed(OMINOUS_TRIAL_KEY_ID);

    if (Tracker.getCategory() instanceof AllBlocks) {
      this.partial = !this.placedHeavyCore;
      this.completionOverride = this.placedHeavyCore || this.obtainedHeavyCore;
    } else {
      this.partial = !this.overOverkillComplete;
      this.completionOverride = this.overOverkillComplete || this.maceCrafted || this.obtainedHeavyCore;
    }
  }

  @Override
  protected void clearAdvancedState() {
    this.obtainedHeavyCore = false;
    this.placedHeavyCore = false;
    this.maceCrafted = false;
    this.overOverkillComplete = false;
    this.ominousVaultsOpened = 0;
  }

  @Override
  protected String getShortStatus() {
    return this.overOverkillComplete ? "Done"
         : this.maceCrafted ? "Mace\0Crafted"
         : "Heavy\0Core";
  }

  @Override
  protected String getLongStatus() {
    if (Tracker.getCategory() instanceof AllBlocks) {
      if (this.placedHeavyCore) return "Heavy\0Core\nPlaced";
    } else {
      if (this.overOverkillComplete) return "Overkill\nComplete";
      if (this.maceCrafted) return "Mace\nCrafted";
    }

    return this.obtainedHeavyCore
           ? "Obtained\nVaults:\0" + this.ominousVaultsOpened
           : "Obtain\nHeavy\0Core";
  }

  @Override
  protected String getCurrentIcon() {
    return !(Tracker.getCategory() instanceof AllBlocks) &&
           (this.overOverkillComplete || this.maceCrafted)
           ? "overoverkill"
           : "heavy_core";
  }
}
