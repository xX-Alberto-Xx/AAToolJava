package org.mcsr.aatool.data.objectives;

import java.util.Map;

import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utils.Pair;

public class CriteriaSet {
  private Uuid closestToCompletion;

  public final Map<String, Criterion> all;
  public final Map<Uuid, Integer> progress;
  public final Advancement owner;
  public final String goal;

  public CriteriaSet(XmlNode node, Advancement owner) {}

  public final Uuid getClosestToCompletion() { return this.closestToCompletion; }

  public final boolean any() {}
  public final int getCount() {}
  public final int getMostCompleted() {}

  public final boolean contains(String criterion) {}

  public final int numberCompletedBy(Uuid player) {}

  public final int percentCompletedBy(Uuid player) {}

  public final void updateStates(WorldState progress) {}

  public final void findPlayerWithMost(WorldState progress) {}

  public final void cloneCriteria(Map<Pair<String, String>, Criterion> dictionary) {}
}
