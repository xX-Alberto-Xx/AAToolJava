package org.mcsr.aatool.data.objectives.complex;

import java.util.ArrayList;
import java.util.List;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Version;

public class Sniffers extends ComplexObjective {
  public static final String ITEM_ID = "minecraft:sniffer_egg";
  private static final String SNIFFER = "minecraft:sniffer";
  private static final String OBTAIN_EGG = "minecraft:husbandry/obtain_sniffer_egg";
  private static final String TWO_BY_TWO = "minecraft:husbandry/bred_all_animals";
  private static final String LITTLE_SNIFFS = "minecraft:husbandry/feed_snifflet";
  private static final String PLANTING_THE_PAST = "minecraft:husbandry/plant_any_sniffer_seed";

  private int estimatedObtained;
  private int estimatedPlaced;

  private boolean eggObtained;
  private boolean sniffersBred;
  private boolean sniffletFed;
  private boolean seedPlanted;

  private boolean doneWithSniffers;

  private final List<String> remainingObjectives = new ArrayList<>();

  public Sniffers() { this.name = "Sniffers"; }

  public final int getEstimatedObtained() { return this.estimatedObtained; }
  public final int getEstimatedPlaced() { return this.estimatedPlaced; }

  public final int getRequired() {
    Version current = Version.tryParse(Tracker.getCurrentVersion());
    return current != null && current.isAtLeast(1, 21) ? 2 : 3;
  }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.estimatedObtained = Math.max(
      progress.timesPickedUp(ITEM_ID) -
      progress.timesDropped(ITEM_ID) -
      progress.timesUsed(ITEM_ID),
      0
    );

    this.estimatedPlaced = Math.max(
      progress.timesUsed(ITEM_ID) - progress.timesMined(ITEM_ID), 0
    );

    this.eggObtained = progress.advancementCompleted(OBTAIN_EGG);
    this.sniffersBred = progress.criterionCompleted(TWO_BY_TWO, SNIFFER);
    this.sniffletFed = progress.advancementCompleted(LITTLE_SNIFFS);
    this.seedPlanted = progress.advancementCompleted(PLANTING_THE_PAST);
    this.updateRemainingObjectives();

    this.doneWithSniffers = this.sniffersBred && this.sniffletFed && this.seedPlanted;
    this.partial = !this.doneWithSniffers;
    this.completionOverride =
      this.estimatedObtained >= this.getRequired() ||
      this.estimatedPlaced > 0 ||
      this.doneWithSniffers;
  }

  @Override
  protected void clearAdvancedState() {
    this.estimatedObtained = 0;
    this.estimatedPlaced = 0;
    this.updateRemainingObjectives();
  }

  private void updateRemainingObjectives() {
    this.remainingObjectives.clear();
    if (!this.eggObtained) this.remainingObjectives.add("Obtain\nEgg");
    if (!this.sniffersBred) this.remainingObjectives.add("Must\0Breed\nSniffers");
    if (!this.sniffletFed) this.remainingObjectives.add("Must\0Feed\nSnifflet");
    if (!this.seedPlanted) this.remainingObjectives.add("Must\0Plant\nSeeds");
  }

  @Override
  protected String getShortStatus() {
    if (this.doneWithSniffers) return "Done";

    if (this.remainingObjectives.size() == 1) {
      if (!this.eggObtained) return "Get\0Egg";
      if (!this.sniffersBred) return "Breed";
      if (!this.sniffletFed) return "Feed";
      if (!this.seedPlanted) return "Plant";
    }

    return this.estimatedPlaced > 0
           ? "Hatching"
           : this.estimatedObtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getLongStatus() {
    return this.doneWithSniffers ? "Done\0With\nSniffers"
         : this.remainingObjectives.size() == 1 ? this.remainingObjectives.get(0)
         : this.estimatedPlaced == 1 ? "Hatching\n" + this.estimatedPlaced + "\0Egg"
         : this.estimatedPlaced > 1 ? "Hatching\n" + this.estimatedPlaced + "\0Eggs"
         : "Eggs\n" + this.estimatedObtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getCurrentIcon() {
    return this.doneWithSniffers ? "sniffer_sniff"
         : this.remainingObjectives.size() == 1 ?
           !this.eggObtained ? "obtain_sniffer_egg" :
           !this.sniffletFed ? "feed_snifflet" :
           !this.seedPlanted ? "plant_any_sniffer_seed" :
           "sniffer_mob"
         : this.estimatedPlaced > 0 ? "sniffer_hatch"
         : "obtain_sniffer_egg";
  }
}
