package org.mcsr.aatool.data.objectives;

import java.util.Map;

import org.mcsr.aatool.data.progress.ProgressState;

public class DeathManifest implements Manifest {
  private Map<String, Death> all;
  private int totalExperienced;

  public DeathManifest() {}

  public final Map<String, Death> getAll() { return this.all; }
  public final int getTotalExperienced() { return this.totalExperienced; }
  public final int getCount() {}

  public final boolean tryGet(String id, /*out */Death death) {}

  public final void clearObjectives() {}

  public final void refreshObjectives() {}

  public final void updateState(ProgressState progress) {}

  public final void updateTotal() {}
}
