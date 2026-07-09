package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonObject;

public class ArmorTrimCriterion extends Criterion {
  private String recipe;
  private boolean obtained;

  public ArmorTrimCriterion(JsonObject obj, Advancement advancement) {
    super(obj, advancement);
    this.recipe = JsonUtils.getString(obj, "recipe", "");
  }

  public final String getRecipe() { return this.recipe; }
  public final boolean isObtained() { return this.obtained; }
  public final boolean isApplied() { return super.completedByDesignated(); }

  @Override
  public boolean completedByDesignated() {
    return this.obtained || super.completedByDesignated();
  }

  @Override
  public boolean isComplete() { return this.obtained || this.isApplied(); }

  @Override
  public void updateState(ProgressState progress) {
    super.updateState(progress);
    this.obtained = progress.recipes.containsKey(this.recipe);
  }
}
