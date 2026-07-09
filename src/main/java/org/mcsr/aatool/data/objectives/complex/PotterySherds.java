package org.mcsr.aatool.data.objectives.complex;

import java.util.Set;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class PotterySherds extends ComplexObjective {
  public static final String BRUSH_ADVANCEMENT = "minecraft:adventure/salvage_sherd";
  public static final String POT_ADVANCEMENT = "minecraft:adventure/craft_decorated_pot_using_only_sherds";

  public static final int REQUIRED = 4;

  public final Set<String> all = Set.of(
    "minecraft:angler_pottery_sherd",
    "minecraft:archer_pottery_sherd",
    "minecraft:arms_up_pottery_sherd",
    "minecraft:blade_pottery_sherd",
    "minecraft:brewer_pottery_sherd",
    "minecraft:burn_pottery_sherd",
    "minecraft:danger_pottery_sherd",
    "minecraft:explorer_pottery_sherd",
    "minecraft:friend_pottery_sherd",
    "minecraft:heart_pottery_sherd",
    "minecraft:heartbreak_pottery_sherd",
    "minecraft:howl_pottery_sherd",
    "minecraft:miner_pottery_sherd",
    "minecraft:mourner_pottery_sherd",
    "minecraft:plenty_pottery_sherd",
    "minecraft:prize_pottery_sherd",
    "minecraft:sheaf_pottery_sherd",
    "minecraft:shelter_pottery_sherd",
    "minecraft:skull_pottery_sherd",
    "minecraft:snort_pottery_sherd"
  );

  private int obtained;

  private boolean advancementsComplete;

  public final int getObtained() { return this.obtained; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.obtained = 0;
    for (String sherdId : this.all) this.obtained += getCount(sherdId, progress);

    this.advancementsComplete =
      progress.advancementCompleted(BRUSH_ADVANCEMENT) &&
      progress.advancementCompleted(POT_ADVANCEMENT);

    this.completionOverride = this.advancementsComplete || this.obtained >= REQUIRED;
  }

  private static int getCount(String id, ProgressState progress) {
    return Math.max(progress.timesPickedUp(id) - progress.timesDropped(id), 0);
  }

  @Override
  protected void clearAdvancedState() {
    this.obtained = 0;
    this.advancementsComplete = false;
  }

  @Override
  protected String getShortStatus() {
    return this.obtained < REQUIRED && this.advancementsComplete
           ? "Done"
           : this.obtained + "\0/\0" + REQUIRED + " Sherds";
  }

  @Override
  protected String getLongStatus() { return this.getShortStatus(); }

  @Override
  protected String getCurrentIcon() { return "pottery_sherd"; }
}
