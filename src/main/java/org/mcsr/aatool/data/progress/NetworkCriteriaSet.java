package org.mcsr.aatool.data.progress;

import java.util.HashSet;
import java.util.Set;

public class NetworkCriteriaSet {
  public final String advancement;
  public final Set<String> list = new HashSet<>();

  public NetworkCriteriaSet(String advancement) { this.advancement = advancement; }
}
