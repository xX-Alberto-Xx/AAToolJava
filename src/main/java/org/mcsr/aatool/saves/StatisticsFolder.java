package org.mcsr.aatool.saves;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class StatisticsFolder extends JsonFolder {
  private static final long MS_PER_TICK = 50;

  public final Duration getInGameTime(JsonStream json) {
    if (json == null) return Duration.ZERO;

    JsonPrimitive ticks = null;
    JsonObject custom = getStatsObject(json, "minecraft:custom");

    if (custom != null) {
      // 1.17+
      ticks = custom.getAsJsonPrimitive("minecraft:play_time");
      // 1.12 - 1.16
      if (ticks == null) ticks = custom.getAsJsonPrimitive("minecraft:play_one_minute");
    }

    // pre-1.12
    if (ticks == null) ticks = (JsonPrimitive) json.get("stat.playOneMinute");

    return ticks != null ? Duration.ofMillis(ticks.getAsLong() * MS_PER_TICK) : Duration.ZERO;
  }

  public final int getKilometersFlown(JsonStream json) {
    int cm = this.getCustomStat(json, "minecraft:aviate_one_cm");
    // Assuming cm >= 0, round cm / 100 / 1000 to the nearest integer, picking the even one in case of a tie
    return (cm + 50_000) / 200_000 + (cm + 149_999) / 200_000;
  }

  public final int getCustomStat(JsonStream json, String name) {
    if (json == null) return 0;

    JsonObject custom = getStatsObject(json, "minecraft:custom");
    if (custom == null) return 0;

    JsonPrimitive value = custom.getAsJsonPrimitive(name);
    return value != null ? value.getAsInt() : 0;
  }

  private static JsonObject getStatsObject(JsonStream json, String key) {
    JsonObject stats = (JsonObject) json.get("stats");
    return stats != null ? stats.getAsJsonObject(key) : null;
  }

  @Override
  protected void update(JsonStream json, WorldState state, Contribution contribution) {
    this.updateGlobalStats(json, state);
    updateCounts("minecraft:picked_up", "pickup", json, state.pickupCounts, contribution.pickupCounts);
    updateCounts("minecraft:dropped", "drop", json, state.dropCounts, contribution.dropCounts);
    updateCounts("minecraft:mined", "mineBlock", json, state.mineCounts, contribution.mineCounts);
    updateCounts("minecraft:crafted", null, json, state.craftCounts, contribution.craftCounts);
    updateCounts("minecraft:used", "useItem", json, state.useCounts, contribution.useCounts);
    updateCounts("minecraft:killed", "killEntity", json, state.killCounts, contribution.killCounts);
  }

  private void updateGlobalStats(JsonStream json, WorldState state) {
    // Use longest IGT of all applicable players
    Duration igt = this.getInGameTime(json);
    if (igt.compareTo(state.inGameTime) > 0) state.inGameTime = igt;

    state.kilometersFlown += this.getKilometersFlown(json);
    state.itemsEnchanted += this.getCustomStat(json, "minecraft:enchant_item");
    state.saveAndQuits += this.getCustomStat(json, "minecraft:leave_game");
    state.damageDealt += this.getCustomStat(json, "minecraft:damage_dealt");
    state.damageTaken += this.getCustomStat(json, "minecraft:damage_taken");
    state.sleeps += this.getCustomStat(json, "minecraft:sleep_in_bed");
    state.deaths += this.getCustomStat(json, "minecraft:deaths");
    state.jumps += this.getCustomStat(json, "minecraft:jump");
  }

  private static void updateCounts(
    String modernKey,
    String oldKey,
    JsonStream json,
    Map<String, Integer> globalCounts,
    Map<String, Integer> playerCounts
  ) {
    if (json != null) {
      JsonObject modernCounts = getStatsObject(json, modernKey);

      if (modernCounts != null) {
        // Count how many of each item this player has picked up
        for (Map.Entry<String, JsonElement> pickup : modernCounts.entrySet()) {
          String name = pickup.getKey();
          int count = pickup.getValue().getAsJsonPrimitive().getAsInt();
          globalCounts.put(name, globalCounts.getOrDefault(name, 0) + count);
          playerCounts.put(name, playerCounts.getOrDefault(name, 0) + count);
        }

        return;
      }
    }

    if (oldKey == null) return;

    // Handle pre-1.12 formatting
    Map<String, Integer> oldVersionCounts = getOldVersionCounts(oldKey, json);

    for (Map.Entry<String, Integer> pickup : oldVersionCounts.entrySet()) {
      String key = pickup.getKey();
      int count = pickup.getValue();
      globalCounts.put(key, globalCounts.getOrDefault(key, 0) + count);
      playerCounts.put(key, playerCounts.getOrDefault(key, 0) + count);
    }
  }

  private static Map<String, Integer> getOldVersionCounts(String group, JsonStream json) {
    Map<String, Integer> counts = new HashMap<>();
    String prefix = "stat." + group + '.';

    for (Map.Entry<String, JsonElement> entry : json) {
      String key = entry.getKey();

      if (key.startsWith(prefix)) {
        counts.put(key.substring(prefix.length()), entry.getValue().getAsJsonPrimitive().getAsInt());
      }
    }

    return counts;
  }
}
