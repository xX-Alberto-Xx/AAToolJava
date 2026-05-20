package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.MultipartObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Cats extends MultipartObjective {
  private static final String TWO_BY_TWO;
  private static final String CAT;

  private boolean catalogueComplete;
  private boolean breedCats;

  public Cats() {}

  @Override
  public String getAdvancementId() {}
  @Override
  public String getCriterion() {}
  @Override
  public String getAction() {}
  @Override
  public String getPastAction() {}
  @Override
  protected String getModernBaseTexture() {}
  @Override
  protected String getOldBaseTexture() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getLongStatus() {}
}
