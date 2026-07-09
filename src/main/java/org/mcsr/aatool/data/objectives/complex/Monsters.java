package org.mcsr.aatool.data.objectives.complex;

import java.util.HashSet;
import java.util.Set;

import org.mcsr.aatool.data.objectives.CriteriaSet;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.MultipartObjective;

public class Monsters extends MultipartObjective {
  private static final Set<String> RAID_MOBS = Set.of(
    "minecraft:ravager", "minecraft:vex", "minecraft:evoker",
    "minecraft:witch", "minecraft:vindicator", "minecraft:pillager"
  );

  protected final Set<String> remainingNonRaidMobs = new HashSet<>();

  public Monsters() { this.name = "Monsters"; }

  private boolean onlyRaidMobsLeft() {
    return this.remainingNonRaidMobs.isEmpty()
        && this.remainingCriteria.size() <= RAID_MOBS.size()
        && !this.remainingCriteria.isEmpty();
  }

  private boolean onlyRaidMobsPlusOneLeft() {
    return this.remainingNonRaidMobs.size() == 1
        && this.remainingCriteria.size() > 1;
  }

  @Override
  public String getAdvancementId() { return "minecraft:adventure/kill_all_mobs"; }
  @Override
  public String getCriterion() { return "Mob"; }
  @Override
  public String getAction() { return "Kill"; }
  @Override
  public String getPastAction() { return "Killed"; }
  @Override
  protected String getModernBaseTexture() { return "kill_all_mobs"; }
  @Override
  protected String getOldBaseTexture() { return "kill_all_mobs_1.12"; }

  @Override
  protected void buildRemainingCriteriaList(CriteriaSet criteria) {
    this.currentCriteria = 0;
    this.requiredCriteria = criteria.getCount();
    this.remainingCriteria.clear();
    this.remainingNonRaidMobs.clear();

    for (Criterion criterion : criteria.all.values()) {
      if (criterion.isComplete()) {
        this.currentCriteria++;
      } else {
        this.remainingCriteria.add(criterion.getName());
        if (!RAID_MOBS.contains(criterion.getId())) this.remainingNonRaidMobs.add(criterion.getName());
        this.lastCriterionIcon = criterion.getIcon();
      }
    }
  }

  @Override
  protected void clearAdvancedState() {
    this.remainingNonRaidMobs.clear();
    super.clearAdvancedState();
  }

  @Override
  protected String getLongStatus() {
    return this.completionOverride ? this.longStatusComplete()
         : this.isOnLastCriterion() ? this.longStatusLast()
         : this.onlyRaidMobsLeft() ? "Awaiting\nRaid"
         : this.onlyRaidMobsPlusOneLeft()
           ? "Needs\0Raid\n&\0" + formatMobName(this.remainingNonRaidMobs.iterator().next())
           : this.longStatusNormal();
  }

  private static String formatMobName(String name) {
    return name
      .replace("Ender Dragon", "Dragon")
      .replace("Zombie Villager", "Zillager")
      .replace("Zombie Piglin", "Ziglin")
      .replace("Piglin Brute", "Brute");
  }

  @Override
  protected String longStatusNormal() {
    return "Mobs\0Killed\n" + this.currentCriteria + "\0/\0" + this.requiredCriteria;
  }

  @Override
  protected String getCurrentIcon() {
    return this.useModernTexture()
           ? this.completionOverride || this.areAllCriteriaCompleted() ? "enchanted_diamond_sword" :
             this.isOnLastCriterion() ? this.lastCriterionIcon :
             this.onlyRaidMobsLeft() || this.onlyRaidMobsPlusOneLeft() ? "enchanted_diamond_sword" :
             this.getModernBaseTexture()
           : this.isOnLastCriterion()
             ? this.lastCriterionIcon
             : this.getOldBaseTexture();
  }
}
