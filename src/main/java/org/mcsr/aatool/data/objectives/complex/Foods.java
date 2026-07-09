package org.mcsr.aatool.data.objectives.complex;

import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAdvancements;
import org.mcsr.aatool.data.objectives.MultipartObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Foods extends MultipartObjective {
  private static final Set<String> HDWGH_FOODS = Set.of("Pufferfish", "Sus Stew", "God Apple");

  private boolean onlyHdwghRemaining;

  @Override
  public String getAdvancementId() { return "minecraft:husbandry/balanced_diet"; }
  @Override
  public String getCriterion() { return "Food"; }
  @Override
  public String getAction() { return "Eat"; }
  @Override
  public String getPastAction() { return "Eaten"; }
  @Override
  protected String getModernBaseTexture() { return "balanced_diet"; }
  @Override
  protected String getOldBaseTexture() { return "balanced_diet_1.12"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    super.updateAdvancedState(progress);

    if (
      Tracker.getCategory() instanceof AllAdvancements &&
      !this.remainingCriteria.isEmpty() &&
      this.remainingCriteria.size() <= 3
    ) {
      this.onlyHdwghRemaining = true;

      for (String food : this.remainingCriteria) {
        if (!HDWGH_FOODS.contains(food)) this.onlyHdwghRemaining = false;
      }
    } else {
      this.onlyHdwghRemaining = false;
    }
  }

  @Override
  protected void clearAdvancedState() {
    super.clearAdvancedState();
    this.onlyHdwghRemaining = false;
  }

  @Override
  protected String getLongStatus() {
    return this.completionOverride ? "All\0Food\nEaten"
         : this.onlyHdwghRemaining ? "Awaiting\nHDWGH"
         : this.isOnLastCriterion() ? "Last\0Food:\n" + this.remainingCriteria.iterator().next()
         : "Food\0Eaten\n" + this.currentCriteria + "\0/\0" + this.requiredCriteria;
  }
}
