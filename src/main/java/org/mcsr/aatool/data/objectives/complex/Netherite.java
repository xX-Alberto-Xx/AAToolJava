package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.progress.ProgressState;

public class Netherite extends AncientDebris {
  @Override
  protected String getCurrentIcon() {
    return this.completedSeriousDedication && this.completedCoverMeInDebris ? "smithing_both"
         : this.completedSeriousDedication ? "smithing_hoe"
         : this.completedCoverMeInDebris ? "smithing_armor"
         : "smithing_none";
  }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    super.updateAdvancedState(progress);
    this.completionOverride |= this.completedSeriousDedication && this.completedCoverMeInDebris;
    this.canBeManuallyChecked = false;
  }
}
