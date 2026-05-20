package org.mcsr.aatool.data.objectives.complex;

import java.util.List;
import java.util.Map;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class SculkBlocks extends ComplexObjective {
  public static final Map<String, String> ALL_SCULK_BLOCKS;

  public List<String> remaining;
  public List<String> obtained;
  public List<String> placed;

  private boolean areAllObtained() {}
  private boolean areAllPlaced() {}
  private boolean isOnLastBlock() {}

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
