package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.progress.ProgressState;

public class Tnt extends AncientDebris {
  @Override
  protected String getShortStatus() {
    return this.areAllNetheriteAdvancementsComplete()
           ? "Done"
           : "TNT: " + this.getEstimatedTnt();
  }

  @Override
  protected String getLongStatus() {
    return this.areAllNetheriteAdvancementsComplete()
           ? "Done\0Mining\nDebris"
           : "TNT: " + this.getEstimatedTnt();
  }

  @Override
  protected String getCurrentIcon() { return "tnt_block"; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    super.updateAdvancedState(progress);
    this.canBeManuallyChecked = false;
  }
}
