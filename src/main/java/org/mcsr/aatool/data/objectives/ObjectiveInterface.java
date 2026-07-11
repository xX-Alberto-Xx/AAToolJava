package org.mcsr.aatool.data.objectives;

import java.time.Instant;

import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.net.Uuid;

public interface ObjectiveInterface {
  boolean isCompletedByAnyone();
  Uuid getFirstToComplete();
  Instant getWhenFirstCompleted();
  String getId();
  String getName();
  String getShortName();
  String getIcon();
  String getFullStatus();
  String getTinyStatus();
  boolean isPartial();
  boolean completedBy(Uuid player);
  Instant whenCompletedBy(Uuid player);
  void updateState(ProgressState state);
}
