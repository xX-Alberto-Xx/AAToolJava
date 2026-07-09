package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.Tracker;

public class BalancedDiet extends SingleAdvancement {
  public static final List<String> SUPPORTED_VERSIONS = List.of("1.17", "1.16");

  private static final String ID = "minecraft:husbandry/balanced_diet";

  public BalancedDiet() {
    this.name = "Balanced Diet";
    this.acronym = "ABD";
    this.objective = "Foods";
    this.action = "Eaten";
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }

  @Override
  public void loadObjectives() {
    Tracker.ADVANCEMENTS.refreshObjectives();
    this.requirement = Tracker.ADVANCEMENTS.tryGetAdvancement(ID).value;
  }
}
