package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.categories.Category;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Version;

public class WitherSkulls extends ComplexObjective {
  public static final String ITEM_ID = "minecraft:wither_skeleton_skull";
  public static final String LEGACY_ITEM_ID = "minecraft.skull";
  private static final String MONSTER_HUNTER = "minecraft:adventure/kill_all_mobs";
  private static final String SUMMON_WITHER = "minecraft:nether/summon_wither";
  private static final String WITHER = "minecraft:wither";
  private static final String WITHER_SKELETON = "minecraft:wither_skeleton";
  private static final String WITHER_ROSE = "minecraft:wither_rose";
  private static final String BEACON = "minecraft:beacon";

  private static final String BEACONATOR = "minecraft:nether/create_full_beacon";
  private static final String LEGACY_BEACONATOR = "achievement.fullBeacon";

  private static final Version BLOCK_ID_CHANGED = new Version(1, 13);
  private static final Version WITHER_KILL_REQUIRED = new Version(1, 16);

  private int estimatedObtained;

  private boolean fullBeaconComplete;

  private boolean rosePlaced;
  private boolean beaconPlaced;
  private boolean witherSummoned;
  private boolean witherKilled;
  private int witherSkeletonsKilled;

  public WitherSkulls() { this.name = "WitherSkulls"; }

  public final int getEstimatedObtained() { return this.estimatedObtained; }

  private static boolean useModernId() {
    Version current = Version.tryParse(Tracker.getCurrentVersion());
    return current == null || current.isAtLeast(BLOCK_ID_CHANGED);
  }

  public final int getRequired() { return Tracker.getCategory() instanceof AllBlocks ? 4 : 3; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    String itemId = useModernId() ? ITEM_ID : LEGACY_ITEM_ID;
    Category category = Tracker.getCategory();

    this.estimatedObtained = Math.max(
      progress.timesPickedUp(itemId) -
      progress.timesDropped(itemId) -
      progress.timesUsed(itemId),
      0
    );

    this.fullBeaconComplete = progress.advancementCompleted(
      category instanceof AllAchievements ? LEGACY_BEACONATOR : BEACONATOR
    );

    // Check wither rose status
    this.rosePlaced = progress.wasUsed(WITHER_ROSE);
    this.beaconPlaced = progress.wasUsed(BEACON);
    this.witherSummoned = progress.advancementCompleted(SUMMON_WITHER);
    this.witherKilled = progress.criterionCompleted(MONSTER_HUNTER, WITHER);
    this.witherSkeletonsKilled = progress.timesKilled(WITHER_SKELETON);

    this.completionOverride =
      this.estimatedObtained >= this.getRequired() || this.witherSummoned || this.witherKilled;

    if (category instanceof AllBlocks) {
      this.partial = !this.rosePlaced && !this.beaconPlaced;
      this.completionOverride |= this.rosePlaced || this.beaconPlaced;
    } else {
      this.partial =
        !this.witherKilled &&
        Version.tryParse(Tracker.getCurrentVersion()).isAtLeast(WITHER_KILL_REQUIRED);

      this.completionOverride |= this.fullBeaconComplete;
    }
  }

  @Override
  protected void clearAdvancedState() {
    this.estimatedObtained = 0;
    this.rosePlaced = false;
    this.beaconPlaced = false;
    this.witherKilled = false;
    this.witherSkeletonsKilled = 0;
  }

  @Override
  protected String getShortStatus() {
    return this.fullBeaconComplete ? "Done"
         : this.witherKilled ? "Done"
         : this.estimatedObtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getLongStatus() {
    return Tracker.getCategory() instanceof AllBlocks && this.beaconPlaced && this.rosePlaced
           ? "Beacon+Rose\nPlaced"
           : this.witherKilled ? "Wither\0Has\nBeen\0Killed"
           : this.witherSummoned ? "Wither\nSummoned"
           : this.estimatedObtained >= this.getRequired()
             ? this.estimatedObtained + "\0/\0" + this.getRequired() + "\nKilled:\0" + this.witherSkeletonsKilled
             : "Skulls\n" + this.estimatedObtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getCurrentIcon() {
    return Tracker.getCategory() instanceof AllBlocks && this.beaconPlaced && this.rosePlaced
           ? "skull_and_beacon"
           : this.witherSummoned || this.witherKilled
             ? "wither_mob"
             : "get_wither_skull";
  }
}
