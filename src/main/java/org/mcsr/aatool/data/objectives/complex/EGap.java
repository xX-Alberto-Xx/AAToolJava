package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class EGap extends ComplexObjective {
  public static final String ITEM_ID;
  public static final String BALANCED_DIET;
  public static final String OVERPOWERED;
  public static final String ENCHANTED_GOLDEN_APPLE;
  public static final String BANNER_RECIPE;

  private static final Version TEXTURE_CHANGED;
  private static final Version ID_ADDED;

  private boolean looted;
  private boolean eaten;

  public final boolean isLooted() { return this.looted; }
  public final boolean isEaten() { return this.eaten; }

  private static boolean useModernTexture() {}

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
}
