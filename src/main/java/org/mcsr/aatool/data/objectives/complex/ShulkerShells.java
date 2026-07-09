package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class ShulkerShells extends Pickup {
  public static final String[] ALL_BOX_VARIANTS = {
    "minecraft:white_shulker_box",
    "minecraft:red_shulker_box",
    "minecraft:orange_shulker_box",
    "minecraft:yellow_shulker_box",
    "minecraft:lime_shulker_box",
    "minecraft:green_shulker_box",
    "minecraft:cyan_shulker_box",
    "minecraft:light_blue_shulker_box",
    "minecraft:blue_shulker_box",
    "minecraft:purple_shulker_box",
    "minecraft:magenta_shulker_box",
    "minecraft:pink_shulker_box",
    "minecraft:brown_shulker_box",
    "minecraft:light_gray_shulker_box",
    "minecraft:gray_shulker_box",
    "minecraft:black_shulker_box",
    "minecraft:shulker_box"
  };

  private boolean allShulkerVariantsPlaced;

  public ShulkerShells() { super("minecraft:shulker_shell"); }

  @Override
  public int getRequired() { return ALL_BOX_VARIANTS.length * 2; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    super.updateAdvancedState(progress);
    this.allShulkerVariantsPlaced = everyBlockPlaced(progress);
    this.completionOverride |= this.allShulkerVariantsPlaced;
    this.partial = !this.allShulkerVariantsPlaced;
  }

  private static boolean everyBlockPlaced(ProgressState progress) {
    for (String block : ALL_BOX_VARIANTS) {
      if (!progress.wasUsed(block)) return false;
    }

    return true;
  }

  @Override
  protected void clearAdvancedState() {
    super.clearAdvancedState();
    this.allShulkerVariantsPlaced = false;
  }

  @Override
  protected String getLongStatus() {
    return this.allShulkerVariantsPlaced ? "All Boxes Placed"
         : this.manuallyChecked ? "Finished Collecting"
         : "Shulkers\n" + this.obtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getCurrentIcon() {
    return this.allShulkerVariantsPlaced ? "shulker_box" : "shulker_shell";
  }
}
