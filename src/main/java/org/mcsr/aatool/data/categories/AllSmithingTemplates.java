package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.ArmorTrimCriterion;
import org.mcsr.aatool.data.objectives.Criterion;

public class AllSmithingTemplates extends SingleAdvancement {
  public static final List<String> SUPPORTED_VERSIONS = List.of("1.20 Snapshot");

  private static final String ID = "custom:all_smithing_templates";

  private int recipesObtained;

  public AllSmithingTemplates() {
    this.name = "All Smithing Templates";
    this.acronym = "AST";
    this.objective = "Templates";
    this.action = "Obtained";
  }

  public final int getRecipesObtained() { return this.recipesObtained; }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }
  @Override
  public int getCompletedCount() { return this.recipesObtained; }

  @Override
  public void loadObjectives() {
    Tracker.ADVANCEMENTS.refreshObjectives();
    this.requirement = Tracker.ADVANCEMENTS.tryGetAdvancement(ID).value;
  }

  @Override
  public void update() {
    super.update();
    this.recipesObtained = 0;
    if (this.requirement == null) return;

    for (Criterion criterion : this.requirement.getCriteria().all.values()) {
      if (((ArmorTrimCriterion) criterion).isObtained()) this.recipesObtained++;
    }
  }
}
