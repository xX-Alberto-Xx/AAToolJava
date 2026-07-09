package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.categories.Category;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Version;

public class Trident extends ComplexObjective {
  private static final String VVF = "minecraft:adventure/very_very_frightening";
  private static final String SURGE = "minecraft:adventure/lightning_rod_with_villager_no_fire";

  private static final Version PIGLIN_HEAD_ADDED = new Version(1, 20);
  private static final Version ANCIENT_CITY_SKELETON_SKULLS = new Version(1, 19);
  private static final Version SURGE_PROTECTOR_ADDED = new Version(1, 17);
  private static final Version TRIDENT_FOUND_IN_VAULTS = new Version(1, 21);

  private boolean obtained;

  private boolean vvfDone;
  private boolean surgeDone;
  private boolean ignoreSurge;

  private boolean piglinHead;
  private boolean zombieHead;
  private boolean creeperHead;
  private boolean skeletonSkull;

  private boolean doneWithHeads;

  public Trident() { this.id = "minecraft:trident"; }

  public final boolean hasEnchantedForegroundLayer() { return this.doneWithHeads; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.obtained = progress.wasPickedUp(this.id);

    Category category = Tracker.getCategory();
    Version current = Version.tryParse(category.getCurrentVersion());
    if (current == null) current = new Version();

    this.canBeManuallyChecked = current.isAtLeast(TRIDENT_FOUND_IN_VAULTS) && !this.obtained;
    this.completionOverride = this.obtained || (this.manuallyChecked && this.canBeManuallyChecked);

    if (category instanceof AllBlocks) {
      // Post-1.19, skeleton skulls are available in ancient city and no longer require thunder
      boolean ancientCitiesExist = current.isAtLeast(ANCIENT_CITY_SKELETON_SKULLS);
      boolean piglinHeadRequired = current.isAtLeast(PIGLIN_HEAD_ADDED);

      this.zombieHead =
        progress.wasUsed("minecraft:zombie_head") ||
        progress.wasPickedUp("minecraft:zombie_head");

      this.creeperHead =
        progress.wasUsed("minecraft:creeper_head") ||
        progress.wasPickedUp("minecraft:creeper_head");

      this.skeletonSkull =
        progress.wasUsed("minecraft:skeleton_skull") ||
        progress.wasPickedUp("minecraft:skeleton_skull") ||
        ancientCitiesExist;

      this.piglinHead =
        progress.wasUsed("minecraft:piglin_head") ||
        progress.wasPickedUp("minecraft:piglin_head");

      this.doneWithHeads = this.zombieHead && this.creeperHead && this.skeletonSkull;
      if (piglinHeadRequired) this.doneWithHeads &= this.piglinHead;

      this.partial = !this.doneWithHeads;
      this.completionOverride |= this.doneWithHeads;
    } else {
      // Get advancements requiring thunder
      this.vvfDone = progress.advancementCompleted(VVF);
      this.surgeDone = progress.advancementCompleted(SURGE);

      // Ignore surge protector, not in the game yet (pre-1.17)
      this.ignoreSurge = current.isBefore(SURGE_PROTECTOR_ADDED);

      boolean advancementsDone = this.vvfDone && (this.surgeDone || this.ignoreSurge);
      this.completionOverride |= advancementsDone;
      this.partial = !advancementsDone;
    }
  }

  @Override
  protected void clearAdvancedState() {
    this.obtained = false;

    this.vvfDone = false;
    this.surgeDone = false;

    this.zombieHead = false;
    this.creeperHead = false;
    this.skeletonSkull = false;
  }

  @Override
  protected String getLongStatus() {
    // Override status with current state of thunder
    return Tracker.getCategory() instanceof AllBlocks
           ? this.doneWithHeads
             ? "Done\0With\nThunder"
             : this.getLongObtainStatus()
           : this.vvfDone && (this.surgeDone || this.ignoreSurge) ? "Done\0With\nThunder"
           // Not done with either, see if we still need trident too
           : this.vvfDone == this.surgeDone || this.ignoreSurge ? this.getLongObtainStatus()
           // Only one of the two thunder-related advancements is complete
           : this.vvfDone ? "Still\0Needs\nSurge\0Prot"
           : "Still\0Needs\nVVF";
  }

  private String getLongObtainStatus() {
    return this.obtained || (this.manuallyChecked && this.canBeManuallyChecked)
           ? "Awaiting\nThunder"
           : "Obtain\nTrident";
  }

  @Override
  protected String getShortStatus() {
    return this.vvfDone && (this.surgeDone || this.ignoreSurge) ? "Done"
         : this.obtained || (this.manuallyChecked && this.canBeManuallyChecked) ? "Obtained"
         : "Trident";
  }

  @Override
  protected String getCurrentIcon() {
    Category category = Tracker.getCategory();

    if (category instanceof AllBlocks) {
      if (!this.doneWithHeads) return "trident";

      Version current = Version.tryParse(category.getCurrentVersion());
      return current != null && current.isAtLeast(PIGLIN_HEAD_ADDED)
             ? "trident_and_heads_1.20"
             : "trident_and_heads";
    }

    return this.vvfDone && !this.surgeDone && !this.ignoreSurge ? "lightning_rod"
         : this.obtained ? "enchanted_trident"
         : "trident";
  }
}
