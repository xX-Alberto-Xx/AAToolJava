package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class Pufferfish extends Pickup {
  public static final String HDWGH_ID = "minecraft:nether/all_effects";
  public static final String BALANCED_DIET_ID = "minecraft:husbandry/balanced_diet";
  public static final String ITEM_ID = "minecraft:pufferfish";

  private boolean advancementsComplete;

  public Pufferfish() { super(ITEM_ID); }

  @Override
  public int getRequired() { return 2; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.advancementsComplete =
      progress.advancementCompleted(HDWGH_ID) &&
      progress.advancementCompleted(BALANCED_DIET_ID);

    super.updateAdvancedState(progress);
  }

  @Override
  protected void clearAdvancedState() {
    super.clearAdvancedState();
    this.advancementsComplete = false;
  }

  @Override
  protected String getShortStatus() {
    return this.advancementsComplete ? "Done" : super.getShortStatus();
  }

  @Override
  protected String getLongStatus() {
    return this.advancementsComplete
           ? "HDWGH\nComplete"
           : "Pufferfish:\n" + this.obtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getCurrentIcon() { return "pufferfish"; }
}
