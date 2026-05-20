package org.mcsr.aatool.data.progress;

import java.util.Set;

import org.mcsr.aatool.data.objectives.ObjectiveInterface;
import org.mcsr.aatool.net.Uuid;

public class Contribution extends ProgressState {
  public final Uuid player;

  public Contribution(Uuid Player) {}

  public Contribution(NetworkContribution network) {}

  @Override
  public Set<Completion> completionsOf(ObjectiveInterface objective) {}
}
