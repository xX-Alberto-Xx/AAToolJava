package org.mcsr.aatool.data.progress;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonSyntaxException;

public class NetworkState {
  public List<NetworkContribution> players = new ArrayList<>();
  public String gameCategory;
  public String gameVersion;
  public double kilometersFlown;
  public int itemsEnchanted;
  public Duration inGameTime;

  public NetworkState() { this.inGameTime = Duration.ZERO; }

  public NetworkState(WorldState state) {
    // Copy world state
    for (Contribution player : state.players.values()) {
      this.players.add(new NetworkContribution(player));
    }

    // Store current game category and version
    this.gameCategory = Tracker.getCurrentCategory();
    this.gameVersion = Tracker.getCurrentVersion();
    this.kilometersFlown = state.kilometersFlown;
    this.itemsEnchanted = state.itemsEnchanted;
    this.inGameTime = state.inGameTime;
  }

  public final String toJsonString() { return JsonUtils.STRICT_GSON.toJson(this); }

  public static NetworkState fromJsonString(String jsonString) {
    try {
      return JsonUtils.STRICT_GSON.fromJson(jsonString, NetworkState.class);
    } catch (JsonSyntaxException ignored) {
      return new NetworkState();
    }
  }
}
