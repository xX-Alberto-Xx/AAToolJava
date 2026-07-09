package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.MultipartObjective;

public class Animals extends MultipartObjective {
  @Override
  public String getAdvancementId() { return "minecraft:husbandry/bred_all_animals"; }
  @Override
  public String getCriterion() { return "Animal"; }
  @Override
  public String getAction() { return "Breed"; }
  @Override
  public String getPastAction() { return "Bred"; }
  @Override
  protected String getModernBaseTexture() { return "golden_carrot"; }
  @Override
  protected String getOldBaseTexture() { return "golden_carrot_1.12"; }

  @Override
  protected String getLongStatus() {
    return this.completionOverride
           ? "All\0Animals\nBred"
           : this.isOnLastCriterion()
             ? "Last\0" + this.getCriterion() + ":\n" + this.remainingCriteria.iterator().next()
             : "Animals\0Bred\n" + this.currentCriteria + "\0/\0" + this.requiredCriteria;
  }
}
