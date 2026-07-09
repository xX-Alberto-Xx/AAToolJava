package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class AncientDebris extends ComplexObjective {
  public static final String ANCIENT_DEBRIS_ID = "minecraft:ancient_debris";
  public static final String NETHERITE_SCRAP_ID = "minecraft:netherite_scrap";
  public static final String NETHERITE_INGOT_ID = "minecraft:netherite_ingot";

  private static final String NETHERITE_BLOCK = "minecraft:netherite_block";
  private static final String OBTAIN_DEBRIS = "minecraft:nether/obtain_ancient_debris";
  private static final String USE_LODESTONE = "minecraft:nether/use_lodestone";
  private static final String NETHERITE_HOE = "minecraft:husbandry/obtain_netherite_hoe";
  private static final String NETHERITE_ARMOR = "minecraft:nether/netherite_armor";
  private static final String TNT = "minecraft:tnt";

  public static final int REQUIRED = 37;

  protected boolean completedHiddenInTheDepths;
  protected boolean completedCountryLode;
  protected boolean completedSeriousDedication;
  protected boolean completedCoverMeInDebris;

  protected boolean craftedNetheriteBlock;
  protected boolean placedNetheriteBlock;

  private boolean allNetheriteAdvancementsComplete;
  private int estimatedDebris;
  private int estimatedTnt;

  public final boolean areAllNetheriteAdvancementsComplete() { return this.allNetheriteAdvancementsComplete; }
  public final int getEstimatedDebris() { return this.estimatedDebris; }
  public final int getEstimatedTnt() { return this.estimatedTnt; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    ProgressState trackerState = Tracker.getState();

    this.estimatedDebris = Math.max(
      progress.timesPickedUp(ANCIENT_DEBRIS_ID) - trackerState.timesDropped(ANCIENT_DEBRIS_ID),
      0
    );

    this.estimatedTnt = Math.max(
      progress.timesPickedUp(TNT) +
      trackerState.timesCrafted(TNT) -
      trackerState.timesUsed(TNT) -
      trackerState.timesDropped(TNT),
      0
    );

    if (Tracker.getCategory() instanceof AllBlocks) {
      // Ignore count if netherite block has been placed
      this.craftedNetheriteBlock = progress.wasCrafted(NETHERITE_BLOCK);
      this.placedNetheriteBlock = progress.wasUsed(NETHERITE_BLOCK);
      this.completionOverride = this.placedNetheriteBlock;
    } else {
      // Ignore count if all netherite related advancements are done
      this.completedHiddenInTheDepths = progress.advancementCompleted(OBTAIN_DEBRIS);
      this.completedCountryLode = progress.advancementCompleted(USE_LODESTONE);
      this.completedSeriousDedication = progress.advancementCompleted(NETHERITE_HOE);
      this.completedCoverMeInDebris = progress.advancementCompleted(NETHERITE_ARMOR);

      this.allNetheriteAdvancementsComplete =
        this.completedHiddenInTheDepths &&
        this.completedCountryLode &&
        this.completedSeriousDedication &&
        this.completedCoverMeInDebris;

      this.completionOverride = this.allNetheriteAdvancementsComplete || this.estimatedDebris >= REQUIRED;
    }

    this.canBeManuallyChecked = !this.completionOverride;
    if (this.manuallyChecked) this.completionOverride = true;

    this.partial = !this.allNetheriteAdvancementsComplete;
  }

  @Override
  protected void clearAdvancedState() {
    this.estimatedDebris = 0;
    this.estimatedTnt = 0;

    this.completedHiddenInTheDepths = false;
    this.completedCountryLode = false;
    this.completedSeriousDedication = false;
    this.completedCoverMeInDebris = false;

    this.allNetheriteAdvancementsComplete = false;

    this.craftedNetheriteBlock = false;
    this.placedNetheriteBlock = false;
  }

  @Override
  protected String getShortStatus() {
    return this.allNetheriteAdvancementsComplete ? "Done"
         : this.manuallyChecked ? "Collected"
         : "Debris: " + this.estimatedDebris;
  }

  @Override
  protected String getLongStatus() {
    return this.allNetheriteAdvancementsComplete ? "Done\0With\nNetherite"
         : this.placedNetheriteBlock ? "Netherite\nPlaced"
         : this.estimatedDebris >= REQUIRED || this.manuallyChecked ? "All\0Debris\nCollected"
         : "Debris:\0" + this.estimatedDebris + "\nTNT:\0" + this.estimatedTnt;
  }

  @Override
  protected String getCurrentIcon() {
    return Tracker.getCategory() instanceof AllBlocks ? "netherite_block"
         : this.allNetheriteAdvancementsComplete ? "supporter_netherite"
         : "obtain_ancient_debris";
  }
}
