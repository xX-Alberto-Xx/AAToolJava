package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;

public interface Manifest {
  void clearObjectives();
  void refreshObjectives();
  void updateState(ProgressState progress);
}
