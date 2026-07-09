package org.mcsr.aatool.data.objectives;

import java.util.HashSet;
import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Version;

public abstract class MultipartObjective extends ComplexObjective {
  private static final Version VILLAGE_AND_PILLAGE_UPDATE = new Version(1, 14);

  protected final Set<String> remainingCriteria = new HashSet<>();

  protected int requiredCriteria;
  protected int currentCriteria;
  protected String lastCriterionIcon;

  public abstract String getAdvancementId();
  public abstract String getCriterion();
  public abstract String getAction();
  public abstract String getPastAction();

  protected abstract String getModernBaseTexture();
  protected abstract String getOldBaseTexture();

  protected String longStatusComplete() {
    return "All\0" + this.getCriterion() + "s\n" + this.getPastAction();
  }

  protected String longStatusLast() {
    return "Last\0" + this.getCriterion() + ":\n" + this.remainingCriteria.iterator().next();
  }

  protected String longStatusNormal() {
    return this.getAction() + '\0' + this.getCriterion() + "s\n" + this.currentCriteria + "\0/\0" + this.requiredCriteria;
  }

  protected Version getTextureUpdateVersion() { return VILLAGE_AND_PILLAGE_UPDATE; }

  protected final boolean useModernTexture() {
    Version current = Version.tryParse(Tracker.getCurrentVersion());
    return current == null || current.isAtLeast(this.getTextureUpdateVersion());
  }

  protected final String getCurrentBaseTexture() {
    return this.useModernTexture() ? this.getModernBaseTexture() : this.getOldBaseTexture();
  }

  protected final boolean isOnLastCriterion() { return this.remainingCriteria.size() == 1; }

  protected final boolean areAllCriteriaCompleted() {
    return this.requiredCriteria > 0 && this.currentCriteria >= this.requiredCriteria;
  }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    // TODO: Tracker
  }

  protected void buildRemainingCriteriaList(CriteriaSet criteria) {
    this.currentCriteria = 0;
    this.requiredCriteria = criteria.getCount();
    this.remainingCriteria.clear();

    for (Criterion criterion : criteria.all.values()) {
      if (criterion.isComplete()) {
        this.currentCriteria++;
      } else {
        this.remainingCriteria.add(criterion.name);
        this.lastCriterionIcon = criterion.icon;
      }
    }
  }

  @Override
  protected void clearAdvancedState() {
    this.currentCriteria = 0;
    this.remainingCriteria.clear();

    // TODO: Tracker
  }

  @Override
  protected String getShortStatus() { return this.currentCriteria + " / " + this.requiredCriteria; }

  protected final void updateRequired() {}

  @Override
  protected String getLongStatus() {
    return this.completionOverride ? this.longStatusComplete()
         : this.isOnLastCriterion() ? this.longStatusLast()
         : this.longStatusNormal();
  }

  @Override
  protected String getCurrentIcon() {
    return this.completionOverride || !this.isOnLastCriterion()
           ? this.getCurrentBaseTexture()
           : this.lastCriterionIcon;
  }
}
