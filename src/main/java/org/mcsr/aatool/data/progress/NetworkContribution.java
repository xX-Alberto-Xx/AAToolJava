package org.mcsr.aatool.data.progress;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.net.Uuid;

public class NetworkContribution {
  public Uuid uuid;
  public Map<String, Instant> advancements = new HashMap<>();
  public Set<NetworkCriteriaSet> multiparts = new HashSet<>();

  public Map<String, Integer> pickup = new HashMap<>();
  public Map<String, Integer> drop = new HashMap<>();
  public Map<String, Integer> mine = new HashMap<>();
  public Map<String, Integer> craft = new HashMap<>();
  public Map<String, Integer> use = new HashMap<>();
  public Map<String, Integer> kill = new HashMap<>();

  public boolean obtainedGodApple;

  private static final Set<String> TRACKED_STATS = Set.of(
    // Items
    "minecraft:trident",
    "minecraft:nautilus_shell",
    "minecraft:enchanted_golden_apple",
    "minecraft:wither_skeleton_skull",
    "minecraft:ancient_debris",
    "minecraft:tnt",
    // Kills
    "minecraft:creeper",
    "minecraft:drowned",
    "minecraft:wither_skeleton",
    "minecraft:phantom",
    "minecraft:cod",
    "minecraft:salmon",
    // Misc stats
    "minecraft:bread",
    "minecraft:ender_pearl",
    "minecraft:netherrack",
    "minecraft:ender_chest",
    "minecraft:lectern",
    "minecraft:sugar_cane",
    "minecraft:conduit",
    "minecraft:bee_nest"
  );

  public NetworkContribution() {}

  public NetworkContribution(Contribution contribution) {
    this.uuid = contribution.player;
    this.obtainedGodApple = contribution.obtainedGodApple;

    // Add advancements
    for (Map.Entry<String, Completion> advancement : contribution.advancements.entrySet()) {
      this.advancements.put(advancement.getKey(), advancement.getValue().timestamp);
    }

    // Add criteria
    Map<String, NetworkCriteriaSet> criteriaSets = new HashMap<>();

    for (String criterion : contribution.criteria.keySet()) {
      String[] tokens = criterion.split(":");

      if (tokens.length == 2) {
        String advancement = tokens[0];
        NetworkCriteriaSet set = criteriaSets.get(advancement);

        if (set == null) {
          set = new NetworkCriteriaSet(advancement);
          criteriaSets.put(advancement, set);
        }

        set.list.add(tokens[1]);
      }
    }

    this.multiparts.addAll(criteriaSets.values());

    // Add stats
    tryAddStats(this.pickup, contribution.pickupCounts);
    tryAddStats(this.drop, contribution.dropCounts);
    tryAddStats(this.mine, contribution.mineCounts);
    tryAddStats(this.craft, contribution.craftCounts);
    tryAddStats(this.use, contribution.useCounts);
    tryAddStats(this.kill, contribution.killCounts);
  }

  private static void tryAddStats(Map<String, Integer> destCounts, Map<String, Integer> srcCounts) {
    if (Tracker.getCategory() instanceof AllBlocks) {
      destCounts.putAll(srcCounts);
      return;
    }

    for (Map.Entry<String, Integer> stat : srcCounts.entrySet()) {
      String key = stat.getKey();
      if (TRACKED_STATS.contains(key)) destCounts.put(key, stat.getValue());
    }
  }
}
