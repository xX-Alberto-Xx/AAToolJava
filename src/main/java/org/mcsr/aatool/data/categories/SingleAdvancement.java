package org.mcsr.aatool.data.categories;

import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.Objective;

public abstract class SingleAdvancement extends Category {
  protected Advancement requirement;

  public final Advancement getRequirement() { return this.requirement; }

  public final Iterable<Criterion> getAllCriteria() {}
  @Override
  public Iterable<Objective> getOverlayObjectives() {}

  @Override
  public int getTargetCount() {}
  @Override
  public int getCompletedCount() {}
}
