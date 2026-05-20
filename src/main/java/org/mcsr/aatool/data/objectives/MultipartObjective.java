package org.mcsr.aatool.data.objectives;

import java.util.Set;

import org.mcsr.aatool.data.progress.ProgressState;

public abstract class MultipartObjective extends ComplexObjective {
  private static final Version VILLAGE_AND_PILLAGE_UPDATE;

  protected final Set<String> remainingCriteria;

  protected int requiredCriteria;
  protected int currentCriteria;
  protected String lastCriterionIcon;

  public abstract String getAdvancementId();
  public abstract String getCriterion();
  public abstract String getAction();
  public abstract String getPastAction();

  protected abstract String getModernBaseTexture();
  protected abstract String getOldBaseTexture();

  protected String longStatusComplete() {}
  protected String longStatusLast() {}
  protected String longStatusNormal() {}

  protected Version getTextureUpdateVersion() {}

  protected final boolean useModernTexture() {}

  protected final String getCurrentBaseTexture() {}

  protected final boolean isOnLastCriterion() {}

  protected final boolean isAllCriteriaCompleted() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  protected void buildRemainingCriteriaList(CriteriaSet criteria) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getShortStatus() {}

  protected final void updateRequired() {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String getCurrentIcon() {}
}
