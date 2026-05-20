package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class HeavyCore extends ComplexObjective {
  public static final String HEAVY_CORE_ID;
  public static final String MACE_ID;
  public static final String OMINOUS_TRIAL_KEY_ID;
  public static final String OVER_OVERKILL_ADVANCEMENT;

  private boolean obtainedHeavyCore;
  private boolean placedHeavyCore;
  private boolean maceCrafted;
  private boolean overOverkillComplete;
  private int ominousVaultsOpened;

  public final boolean obtainedHeavyCore() { return this.obtainedHeavyCore; }
  public final boolean placedHeavyCore() { return this.placedHeavyCore; }
  public final boolean isMaceCrafted() { return this.maceCrafted; }
  public final boolean isOverOverkillComplete() { return this.overOverkillComplete; }
  public final int getOminousVaultsOpened() { return this.ominousVaultsOpened; }

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
