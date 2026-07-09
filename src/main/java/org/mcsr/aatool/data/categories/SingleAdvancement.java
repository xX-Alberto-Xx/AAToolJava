package org.mcsr.aatool.data.categories;

import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.Objective;

public abstract class SingleAdvancement extends Category {
  protected Advancement requirement;

  public final Advancement getRequirement() { return this.requirement; }

  public final Iterable<Criterion> getAllCriteria() {
    return this.requirement.getCriteria().all.values();
  }

  @Override
  public Iterable<? extends Objective> getOverlayObjectives() {
    return this.getAllCriteria();
  }

  @Override
  public int getTargetCount() {
    return this.requirement != null ? this.requirement.getCriteria().getCount() : 0;
  }

  @Override
  public int getCompletedCount() {
    return this.requirement != null ? this.requirement.getCriteria().getMostCompleted() : 0;
  }
}
