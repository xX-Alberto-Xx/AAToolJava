package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class NetheriteUpgrade extends ComplexObjective {
  public static final String RECIPE = "minecraft:recipes/misc/netherite_upgrade_smithing_template";

  private boolean obtained;

  public NetheriteUpgrade() { this.icon = "upgrade_netherite"; }

  public final boolean isObtained() { return this.obtained; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.completionOverride = progress.recipes.containsKey(RECIPE);
  }

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getLongStatus() { return "Netherite Up"; }
  @Override
  protected String getShortStatus() { return "Netherite Up"; }
}
