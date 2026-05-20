package org.mcsr.aatool.data.objectives;

import java.util.Map;

import org.mcsr.aatool.data.progress.ProgressState;

public abstract class ComplexObjective extends Objective {
  public static final Map<String, Class> TYPES;

  private String fullStatus;
  private String tinyStatus;

  public ComplexObjective() {}

  public static boolean tryCreateInstance(String type, /*out */ComplexObjective objective) {}

  @Override
  public String getFullStatus() {}
  @Override
  public String getTinyStatus() {}

  protected abstract String getLongStatus();
  protected abstract String getShortStatus();
  protected abstract void updateAdvancedState(ProgressState progress);
  protected abstract void clearAdvancedState();

  protected String getCurrentIcon() {}

  @Override
  public final void updateState(ProgressState progress) {}

  public final void refreshStatus() {}
}
