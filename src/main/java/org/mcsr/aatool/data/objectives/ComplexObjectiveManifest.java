package org.mcsr.aatool.data.objectives;

import java.util.Map;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.data.progress.ProgressState;

public class ComplexObjectiveManifest implements Manifest {
  private Map<String, ComplexObjective> allByName;

  public ComplexObjectiveManifest() {}

  public final Map<String, ComplexObjective> getAllByName() { return this.allByName; }

  public final boolean tryGet(String typeName, /*out */ComplexObjective objective) {}

  public final void clearObjectives() {}

  public final void refreshObjectives() {}

  private void addPickup(String id, String name, int required) {}

  public final void updateState(ProgressState progress) {}

  public final void updateDynamicIcons(Time time) {}
}
