package org.mcsr.aatool.data.progress;

import java.util.List;

public class NetworkState {
  public List<NetworkContribution> players;
  public String gameCategory;
  public String gameVersion;
  public double kilometersFlown;
  public int itemsEnchanted;
  public TimeSpan inGameTime;

  public NetworkState() {}

  public NetworkState(WorldState state) {}

  public final String toJsonString() {}

  public static NetworkState fromJsonString(String jsonString) {}
}
