package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.net.Uuid;

public class Advancement extends Objective {
  protected Uuid designatedPlayer;
  protected boolean designationLinked;
  private CriteriaSet criteria;

  public final boolean hiddenWhenRelaxed;
  public final boolean hiddenWhenCompact;
  public final boolean usedInHalfPercent;

  public Advancement(XmlNode node) {}

  public final Uuid designatedPlayer() { return this.designatedPlayer; }
  public final boolean isDesignationLinked() { return this.designationLinked; }
  public final CriteriaSet getCriteria() { return this.criteria; }

  @Override
  public String getFullStatus() {}
  @Override
  public String getTinyStatus() {}

  public final boolean hasCriteria() {}

  @Override
  public boolean isComplete() {}

  public final void linkDesignation() {}
  public final void unlinkDesignation() {}

  public final void designate(Uuid id) {}

  public final Uuid getDesignatedPlayer() {}

  @Override
  public void updateState(ProgressState progress) {}

  protected final void parseCriteria(XmlNode advancementNode) {}
}
