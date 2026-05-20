package org.mcsr.aatool.data.progress;

import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.net.Uuid;

public class NetworkContribution {
  public Uuid uuid;
  public Map<String, DateTime> advancements;
  public Set<NetworkCriteriaSet> multiparts;

  public Map<String, Integer> pickup;
  public Map<String, Integer> drop;
  public Map<String, Integer> mine;
  public Map<String, Integer> craft;
  public Map<String, Integer> use;
  public Map<String, Integer> kill;

  public boolean obtainedGodApple;

  private static final Set<String> TRACKED_STATS;

  public NetworkContribution() {}
  public NetworkContribution(Contribution contribution) {}

  private void tryAddStat(Map<String, Integer> counts, KeyValuePair<String, Integer> stat) {}
}
