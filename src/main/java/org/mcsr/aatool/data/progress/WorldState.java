package org.mcsr.aatool.data.progress;

import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.data.objectives.ObjectiveInterface;
import org.mcsr.aatool.net.Uuid;

public class WorldState extends ProgressState {
  public static final WorldState EMPTY;

  public Map<Uuid, Contribution> players;

  public WorldState() {}

  public WorldState(NetworkState state) {}

  private void copyStats(Map<String, Integer> source, Map<String, Integer> destination) {}

  @Override
  public Set<Completion> completionsOf(ObjectiveInterface objective) {}

  public final void syncDeathMessages() {}
}
