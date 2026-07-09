package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.MultipartObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Cats extends MultipartObjective {
  private static final String TWO_BY_TWO = "minecraft:husbandry/bred_all_animals";
  private static final String CAT = "minecraft:cat";

  private boolean catalogueComplete;
  private boolean breedCats;

  public Cats() { this.icon = "complete_catalogue"; }

  @Override
  public String getAdvancementId() { return "minecraft:husbandry/complete_catalogue"; }
  @Override
  public String getCriterion() { return "Cat"; }
  @Override
  public String getAction() { return "Tame"; }
  @Override
  public String getPastAction() { return "Tamed"; }
  @Override
  protected String getModernBaseTexture() { return "complete_catalogue"; }
  @Override
  protected String getOldBaseTexture() { return "complete_catalogue"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    super.updateAdvancedState(progress);
    this.catalogueComplete = progress.advancementCompleted(this.getAdvancementId());
    this.breedCats = progress.criterionCompleted(TWO_BY_TWO, CAT);
    this.completionOverride &= this.breedCats;
  }

  @Override
  protected void clearAdvancedState() {
    super.clearAdvancedState();
    this.breedCats = false;
  }

  @Override
  protected String getLongStatus() {
    return this.completionOverride ? "Done\0With\nCats"
         : this.remainingCriteria.size() == 1 ? "Last\0Cat:\n" + this.remainingCriteria.iterator().next()
         : this.catalogueComplete && !this.breedCats ? "Needs\0To\nBreed\0Cats"
         : "Cats\0Tamed\n" + this.currentCriteria + "\0/\0" + this.requiredCriteria;
  }
}
