package org.mcsr.aatool.data.objectives.complex;

import java.util.ArrayList;
import java.util.List;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Version;

public class Bees extends ComplexObjective {
  public static final String BLOCK_ID = "minecraft:bee_nest";

  private static final String TOTAL_BEELOCATION = "minecraft:husbandry/silk_touch_nest";
  private static final String BEE_OUR_GUEST = "minecraft:husbandry/safely_harvest_honey";
  private static final String BALANCED_DIET = "minecraft:husbandry/balanced_diet";
  private static final String TWO_BY_TWO = "minecraft:husbandry/bred_all_animals";
  private static final String STICKY_SITUATION = "minecraft:adventure/honey_block_slide";
  private static final String WAX_ON = "minecraft:husbandry/wax_on";
  private static final String WAX_OFF = "minecraft:husbandry/wax_off";
  private static final String HONEY_BOTTLE = "honey_bottle";
  private static final String BEE = "minecraft:bee";

  private static final String HONEY_BLOCK = "minecraft:honey_block";
  private static final String HONEY_COMB_BLOCK = "minecraft:honeycomb_block";

  private static final String EMPTY_TEXTURE = "bee_nest";
  private static final String FULL_TEXTURE = "bee_nest_full";

  private static final Version CAVES_AND_CLIFFS_PART_ONE = new Version(1, 17);

  public static final String[] ALL_WAXED_COPPER = {
    "minecraft:waxed_copper_block",
    "minecraft:waxed_cut_copper",
    "minecraft:waxed_cut_copper_stairs",
    "minecraft:waxed_cut_copper_slab",
    "minecraft:waxed_exposed_copper",
    "minecraft:waxed_exposed_cut_copper",
    "minecraft:waxed_exposed_cut_copper_stairs",
    "minecraft:waxed_exposed_cut_copper_slab",
    "minecraft:waxed_weathered_copper",
    "minecraft:waxed_weathered_cut_copper",
    "minecraft:waxed_weathered_cut_copper_stairs",
    "minecraft:waxed_weathered_cut_copper_slab",
    "minecraft:waxed_oxidized_copper",
    "minecraft:waxed_oxidized_cut_copper",
    "minecraft:waxed_oxidized_cut_copper_stairs",
    "minecraft:waxed_oxidized_cut_copper_slab"
  };

  public static final String[] ALL_CANDLES = {
    "minecraft:white_candle",
    "minecraft:red_candle",
    "minecraft:orange_candle",
    "minecraft:yellow_candle",
    "minecraft:lime_candle",
    "minecraft:green_candle",
    "minecraft:cyan_candle",
    "minecraft:light_blue_candle",
    "minecraft:blue_candle",
    "minecraft:purple_candle",
    "minecraft:magenta_candle",
    "minecraft:pink_candle",
    "minecraft:brown_candle",
    "minecraft:light_gray_candle",
    "minecraft:gray_candle",
    "minecraft:black_candle",
    "minecraft:candle"
  };

  private int estimatedCount;
  private int estimatedPlaced;

  // All Advancements
  private boolean totalBeelocation;
  private boolean beeOurGuest;
  private boolean stickySituation;
  private boolean waxOn;
  private boolean waxOff;
  private boolean drinkHoney;
  private boolean breedBees;

  private boolean balancedDiet;
  private boolean twoByTwo;

  // All Blocks
  private boolean honeyBlockPlaced;
  private boolean honeycombBlockPlaced;
  private boolean allCandlesPlaced;
  private boolean allWaxedCopperPlaced;

  private final List<String> remainingObjectives = new ArrayList<>();
  private boolean doneWithBees;

  public Bees() { this.name = "Bees"; }

  private boolean areCopperAndCandlesAdded() {
    Version current = Version.tryParse(Tracker.getCategory().getCurrentVersion());
    return current != null && current.isAtLeast(CAVES_AND_CLIFFS_PART_ONE);
  }

  private boolean areWaxAdvancementsAdded() {
    Version current = Version.tryParse(Tracker.getCategory().getCurrentVersion());
    return current != null && current.isAtLeast(CAVES_AND_CLIFFS_PART_ONE);
  }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.estimatedCount = Math.max(
      progress.timesPickedUp(BLOCK_ID) -
      progress.timesDropped(BLOCK_ID) -
      progress.timesUsed(BLOCK_ID),
      0
    );

    this.estimatedPlaced = progress.timesUsed(BLOCK_ID);

    if (Tracker.getCategory() instanceof AllBlocks) {
      // Check blocks
      this.honeyBlockPlaced = progress.wasUsed(HONEY_BLOCK);
      this.honeycombBlockPlaced = progress.wasUsed(HONEY_COMB_BLOCK);

      this.allWaxedCopperPlaced = this.allWaxedCopperPlaced(progress);
      this.allCandlesPlaced = this.allCandlesPlaced(progress);
    } else {
      // Check advancements
      this.totalBeelocation = progress.advancementCompleted(TOTAL_BEELOCATION);
      this.beeOurGuest = progress.advancementCompleted(BEE_OUR_GUEST);
      this.stickySituation = progress.advancementCompleted(STICKY_SITUATION);
      this.waxOn = progress.advancementCompleted(WAX_ON);
      this.waxOff = progress.advancementCompleted(WAX_OFF);

      this.balancedDiet = progress.advancementCompleted(BALANCED_DIET);
      this.twoByTwo = progress.advancementCompleted(TWO_BY_TWO);

      this.drinkHoney = progress.criterionCompleted(BALANCED_DIET, HONEY_BOTTLE);
      this.breedBees = progress.criterionCompleted(TWO_BY_TWO, BEE);
    }

    this.buildRemainingObjectiveList();
    this.doneWithBees = this.remainingObjectives.isEmpty();
    this.partial = !this.doneWithBees;
    this.completionOverride = this.doneWithBees || this.estimatedPlaced > 0;
  }

  private void buildRemainingObjectiveList() {
    this.remainingObjectives.clear();

    if (Tracker.getCategory() instanceof AllBlocks) {
      // Check blocks
      if (!this.honeyBlockPlaced) this.remainingObjectives.add("Still\0Needs\nHoney\0Block");
      if (!this.honeycombBlockPlaced) this.remainingObjectives.add("Still\0Needs\nHoneycomb");

      if (this.areCopperAndCandlesAdded() && !this.allCandlesPlaced) {
        this.remainingObjectives.add("Still\0Needs\nCandles");
      }

      if (this.areCopperAndCandlesAdded() && !this.allWaxedCopperPlaced) {
        this.remainingObjectives.add("Still\0Needs\nWaxed\0Copper");
      }
    } else {
      // Check advancements
      if (!this.totalBeelocation) this.remainingObjectives.add("Still\0Needs\nBeelocation");
      if (!this.beeOurGuest) this.remainingObjectives.add("Must\0Harvest\nHoney");
      if (!this.stickySituation) this.remainingObjectives.add("Still\0Needs\nHoney\0Block");

      if (!this.drinkHoney && !this.balancedDiet) {
        this.remainingObjectives.add("Needs\0To\nDrink\0Honey");
      }

      if (!this.breedBees && !this.twoByTwo) {
        this.remainingObjectives.add("Needs\0To\nBreed\0Bees");
      }

      if (this.areWaxAdvancementsAdded() && !this.waxOn) {
        this.remainingObjectives.add("Still\0Needs\nWax\0On");
      }

      if (this.areWaxAdvancementsAdded() && !this.waxOff) {
        this.remainingObjectives.add("Still\0Needs\nWax\0Off");
      }
    }
  }

  private boolean allWaxedCopperPlaced(ProgressState progress) {
    for (String copperVariant : ALL_WAXED_COPPER) {
      if (!progress.wasUsed(copperVariant)) return false;
    }

    return true;
  }

  private boolean allCandlesPlaced(ProgressState progress) {
    for (String candleVariant : ALL_CANDLES) {
      if (!progress.wasUsed(candleVariant)) return false;
    }

    return true;
  }

  @Override
  protected void clearAdvancedState() {
    this.remainingObjectives.clear();

    this.estimatedCount = 0;
    this.estimatedPlaced = 0;

    this.totalBeelocation = false;
    this.beeOurGuest = false;
    this.stickySituation = false;
    this.waxOn = false;
    this.waxOff = false;
    this.drinkHoney = false;

    this.balancedDiet = false;
    this.twoByTwo = false;

    this.honeyBlockPlaced = false;
    this.honeycombBlockPlaced = false;
    this.allCandlesPlaced = false;
    this.allWaxedCopperPlaced = false;

    this.doneWithBees = false;
  }

  @Override
  protected String getShortStatus() {
    return this.doneWithBees ? "Done" : "Hives:\0" + this.estimatedCount;
  }

  @Override
  protected String getLongStatus() {
    return this.doneWithBees ? "Done\0With\nBees"
         : this.remainingObjectives.size() == 1 ? this.remainingObjectives.get(0)
         : this.estimatedPlaced > 0
           ? this.estimatedPlaced + "\0Hive" + (this.estimatedPlaced == 1 ? "" : "s") + "\nPlaced"
           : this.estimatedCount + "\0Hive" + (this.estimatedCount == 1 ? "" : "s") + "\nCollected";
  }

  @Override
  protected String getCurrentIcon() {
    return this.completionOverride ? "bee_nest_full_pickup" : "bee_nest_pickup";
  }
}
