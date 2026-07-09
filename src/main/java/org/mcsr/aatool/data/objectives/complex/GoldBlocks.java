package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Version;

public class GoldBlocks extends ComplexObjective {
  public static final String ITEM_ID = "minecraft:gold_block";
  public static final String LEGACY_ITEM_ID = "minecraft.gold_block";
  public static final String GOLD_INGOT_ID = "minecraft:gold_ingot";
  public static final float INGOTS_PER_BLOCK = 9;

  public static final int REQUIRED = 164;

  private static final String BEACONATOR = "minecraft:nether/create_full_beacon";
  private static final String LEGACY_BEACONATOR = "achievement.fullBeacon";

  private static final Version BLOCK_ID_CHANGED = new Version(1, 13);
  private static final Version TEXTURE_CHANGED = new Version(1, 14);

  private boolean fullBeaconComplete;
  private int estimatedBlocks;

  private static boolean useModernId() {
    Version current = Version.tryParse(Tracker.getCurrentVersion());
    return current == null || current.isAtLeast(BLOCK_ID_CHANGED);
  }

  private static boolean useModernTexture() {
    Version current = Version.tryParse(Tracker.getCurrentVersion());
    return current == null || current.isAtLeast(TEXTURE_CHANGED);
  }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.updatePreciseGoldEstimate(progress);

    this.fullBeaconComplete = progress.advancementCompleted(
      Tracker.getCategory() instanceof AllAchievements ? LEGACY_BEACONATOR : BEACONATOR
    );

    this.completionOverride =
      this.estimatedBlocks >= REQUIRED ||
      this.fullBeaconComplete ||
      this.manuallyChecked;

    this.canBeManuallyChecked = this.estimatedBlocks < REQUIRED && !this.fullBeaconComplete;
    if (this.manuallyChecked) this.completionOverride = true;

    this.partial = !this.fullBeaconComplete;
  }

  private void updatePreciseGoldEstimate(ProgressState progress) {
    this.estimatedBlocks = getPreciseEstimate(progress);
  }

  public static int getPreciseEstimate(ProgressState progress) {
    String itemId = useModernId() ? ITEM_ID : LEGACY_ITEM_ID;

    return Math.max(
      // Account for blocks
      progress.timesPickedUp(itemId) - progress.timesDropped(itemId) - progress.timesUsed(itemId)
      + Math.round((
        // Account for ingots
        progress.timesPickedUp(GOLD_INGOT_ID) - progress.timesDropped(GOLD_INGOT_ID)
        // Account for crafting of armor/tools
        - progress.timesCrafted("minecraft:golden_pickaxe") * 3
        - progress.timesCrafted("minecraft:golden_helmet") * 5
        - progress.timesCrafted("minecraft:golden_chestplate") * 8
        - progress.timesCrafted("minecraft:golden_leggings") * 7
        - progress.timesCrafted("minecraft:golden_boots") * 4
        // Account for crafting of foods
        - progress.timesCrafted("minecraft:golden_carrot") * 8 / 9
        - progress.timesCrafted("minecraft:golden_apple") * 8
      ) / INGOTS_PER_BLOCK),
      0
    );
  }

  @Override
  protected void clearAdvancedState() {
    this.fullBeaconComplete = false;
    this.estimatedBlocks = 0;
  }

  @Override
  protected String getShortStatus() {
    return this.fullBeaconComplete ? "Done"
         : this.manuallyChecked ? "Collected"
         : this.estimatedBlocks + "\0/\0" + REQUIRED;
  }

  @Override
  protected String getLongStatus() {
    return this.fullBeaconComplete ? "Full\0Beacon\nConstructed"
         : this.manuallyChecked ? "All\0Gold\nCollected"
         : this.estimatedBlocks > 0 ? "Gold\0Estimate\n" + this.estimatedBlocks + "\0/\0" + REQUIRED
         : "Gold\0Blocks\n0\0/\0" + REQUIRED;
  }

  @Override
  protected String getCurrentIcon() {
    return this.fullBeaconComplete ? "beacon"
         : useModernTexture() ? "gold_blocks"
         : "gold_block_1.12";
  }
}
