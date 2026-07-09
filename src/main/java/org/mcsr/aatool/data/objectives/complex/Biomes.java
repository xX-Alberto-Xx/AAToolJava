package org.mcsr.aatool.data.objectives.complex;

import java.util.HashSet;
import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.data.categories.AllAdvancements;
import org.mcsr.aatool.data.categories.Category;
import org.mcsr.aatool.data.objectives.CriteriaSet;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.MultipartObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Biomes extends MultipartObjective {
  private static final Set<String> MEGA_TAIGA_BIOMES = Set.of(
    /* 1.18 */ "minecraft:old_growth_spruce_taiga", "minecraft:old_growth_pine_taiga",
    /* 1.13 */ "minecraft:giant_tree_taiga", "minecraft:giant_tree_taiga_hills",
    /* 1.12 */ "redwood_taiga", "redwood_taiga_hills",
    /* 1.11 */ "Mega Taiga", "Mega Taiga Hills"
  );

  private static final Set<String> MUSHROOM_BIOMES = Set.of(
    /* 1.13 */ "minecraft:mushroom_fields", "minecraft:mushroom_field_shore",
    /* 1.12 */ "mushroom_island", "mushroom_island_shore",
    /* 1.11 */ "MushroomIsland", "MushroomIslandShore"
  );

  private static final Set<String> BADLANDS_BIOMES = Set.of(
    /* 1.18 */ "minecraft:badlands", "minecraft:wooded_badlands", "minecraft:badlands_plateau", "minecraft:wooded_badlands_plateau",
    /* 1.13 */ "minecraft:eroded_badlands",
    /* 1.12 */ "mesa", "mesa_clear", "mesa_clear_rock",
    /* 1.11 */ "Mesa", "Mesa Plateau", "Mesa Plateau F"
  );

  private static final Set<String> BAMBOO_BIOMES = Set.of(
    "minecraft:bamboo_jungle", "minecraft:bamboo_jungle_hills"
  );

  private boolean onlyMushroomLeft;
  private boolean onlyMegaTaigaLeft;
  private boolean onlyBadlandsLeft;
  private boolean onlyBambooLeft;

  protected final Set<String> remainingIds = new HashSet<>();

  @Override
  public String getAdvancementId() {
    return "1.11".equals(Tracker.getCurrentVersion())
           ? "achievement.exploreAllBiomes"
           : "minecraft:adventure/adventuring_time";
  }

  @Override
  public String getCriterion() { return "Biome"; }
  @Override
  public String getAction() { return "Visit"; }
  @Override
  public String getPastAction() { return "Visited"; }
  @Override
  protected String getModernBaseTexture() { return "adventuring_time"; }
  @Override
  protected String getOldBaseTexture() { return "adventuring_time_1.12"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    super.updateAdvancedState(progress);
    this.onlyMegaTaigaLeft = this.onlyGroupRemaining(MEGA_TAIGA_BIOMES, 2);
    this.onlyMushroomLeft = this.onlyGroupRemaining(MUSHROOM_BIOMES, 2);
    this.onlyBadlandsLeft = this.onlyGroupRemaining(BADLANDS_BIOMES, 3);
    this.onlyBambooLeft = this.onlyGroupRemaining(BAMBOO_BIOMES, 2);
  }

  @Override
  protected void buildRemainingCriteriaList(CriteriaSet criteria) {
    this.currentCriteria = 0;
    this.requiredCriteria = criteria.getCount();
    this.remainingIds.clear();
    this.remainingCriteria.clear();

    for (Criterion criterion : criteria.all.values()) {
      if (criterion.isComplete()) {
        this.currentCriteria++;
      } else {
        this.remainingCriteria.add(criterion.getName());
        this.remainingIds.add(criterion.getId());
        this.lastCriterionIcon = criterion.getIcon();
      }
    }
  }

  private boolean onlyGroupRemaining(Set<String> group, int maxRemaining) {
    Category category = Tracker.getCategory();

    return (category instanceof AllAdvancements || category instanceof AllAchievements) &&
           !this.remainingCriteria.isEmpty() &&
           this.remainingCriteria.size() <= maxRemaining &&
           group.containsAll(this.remainingIds);
  }

  @Override
  protected void clearAdvancedState() {
    this.onlyMegaTaigaLeft = false;
    this.onlyMushroomLeft = false;
    this.onlyBadlandsLeft = false;
    this.onlyBambooLeft = false;
    super.clearAdvancedState();
  }

  @Override
  protected String getLongStatus() {
    return this.isOnLastCriterion() ? super.getLongStatus()
         : this.onlyMegaTaigaLeft ? "Still\0Needs\nMega Taiga"
         : this.onlyMushroomLeft ? "Still\0Needs\nMushroom"
         : this.onlyBadlandsLeft ? "Still\0Needs\nBadlands"
         : this.onlyBambooLeft ? "Still\0Needs\nBamboo"
         : super.getLongStatus();
  }

  @Override
  protected String longStatusNormal() {
    return "Biomes\n" + this.currentCriteria + "\0/\0" + this.requiredCriteria;
  }

  @Override
  protected String longStatusLast() {
    return "Last Biome:\n" + formatBiomeName(this.remainingCriteria.iterator().next());
  }

  private static String formatBiomeName(String name) {
    return name
      .replace(" Mountain", " Mtn")
      .replace(" Plateau", "-Plat")
      .replace("Mushroom Fields", "Mushroom")
      .replace("Mushroom Shore", "Mush-Shore")
      .replace("Bamboo Jungle", "Bamboo")
      .replace("Deep Cold Ocean", "Deep Cold")
      .replace("Lukewarm Ocean", "Lukewarm")
      .replace(" Growth", "")
      .replace(" ", "\0");
  }

  @Override
  protected String getCurrentIcon() {
    boolean useModernTexture = this.useModernTexture();

    return useModernTexture && (this.completionOverride || this.areAllCriteriaCompleted())
           ? "enchanted_diamond_boots"
           : this.isOnLastCriterion() ? this.lastCriterionIcon :
             this.onlyMegaTaigaLeft ? "giant_tree_taiga" :
             this.onlyMushroomLeft ? "mushroom_fields" :
             this.onlyBadlandsLeft ? "badlands" :
             this.onlyBambooLeft ? "bamboo_jungle" :
             useModernTexture ? this.getModernBaseTexture() : this.getOldBaseTexture();
  }
}
