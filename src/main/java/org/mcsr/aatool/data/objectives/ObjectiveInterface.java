package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.net.Uuid;

public interface ObjectiveInterface {
  public boolean isCompletedByAnyone();
  public Uuid getFirstToComplete();
  public DateTime getWhenFirstCompleted();
  public String getId();
  public String getName();
  public String getShortName();
  public String getIcon();
  public String getFullStatus();
  public String getTinyStatus();
  public boolean isPartial();
  public boolean completedBy(Uuid player);
  public DateTime whenCompletedBy(Uuid player);
  public void updateState(ProgressState state);
}
