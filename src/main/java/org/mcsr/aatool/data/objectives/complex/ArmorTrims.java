package org.mcsr.aatool.data.objectives.complex;

import java.util.List;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class ArmorTrims extends ComplexObjective {
  public static final List<String> RECIPES;

  public static final String ADVANCEMENT_ID;
  public static final String CATEGORY_ID;

  public List<String> required;
  public List<String> remaining;
  public List<String> obtained;
  public List<String> applied;

  public ArmorTrims() {}

  private boolean allObtained() {}
  private boolean allApplied() {}
  private boolean isOnLast() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getCurrentIcon() {}

  public final void updateDynamicIcon(Time time) {}

  public static String iconName(String id) {}

  public static String friendlyName(String id) {}
}
