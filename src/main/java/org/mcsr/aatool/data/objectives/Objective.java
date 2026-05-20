package org.mcsr.aatool.data.objectives;

import java.util.Set;

import org.mcsr.aatool.data.progress.Completion;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.enums.FrameType;
import org.mcsr.aatool.net.Uuid;

public abstract class Objective implements ObjectiveInterface {
  protected String id;
  protected String icon;
  protected String name;
  protected String shortName;

  protected boolean canBeManuallyChecked;
  protected boolean completionOverride;
  public boolean manuallyChecked;
  protected boolean partial;

  protected Set<Completion> completions;
  protected Completion firstCompletion;
  protected FrameType frame;

  public Objective() {}
  public Objective(XmlNode node/* = null*/) {}

  @Override
  public final String getId() { return this.id; }
  @Override
  public final String getIcon() { return this.icon; }
  @Override
  public final String getName() { return this.name; }
  @Override
  public final String getShortName() { return this.shortName; }

  @Override
  public abstract String getFullStatus();
  @Override
  public abstract String getTinyStatus();

  public final boolean canBeManuallyChecked() { return this.canBeManuallyChecked; }
  public final boolean hasCompletionOverride() { return this.completionOverride; }
  @Override
  public final boolean isPartial() { return this.partial; }

  public final Set<Completion> getCompletions() { return this.completions; }
  public final Completion getFirstCompletion() { return this.firstCompletion; }
  public final FrameType getFrame() { return this.frame; }

  public void toggleManualCheck() {}

  public boolean isComplete() {}

  @Override
  public final Uuid getFirstToComplete() {}
  @Override
  public final DateTime getWhenFirstCompleted() {}

  @Override
  public boolean isCompletedByAnyone() {}

  @Override
  public final boolean completedBy(Uuid player) {}

  @Override
  public final DateTime whenCompletedBy(Uuid player) {}

  @Override
  public void updateState(ProgressState progress) {}
}
