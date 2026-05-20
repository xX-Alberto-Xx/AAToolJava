package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;

public interface Manifest {
  public void clearObjectives();
  public void refreshObjectives();
  public void updateState(ProgressState progress);
}
