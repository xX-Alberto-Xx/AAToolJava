package org.mcsr.aatool.data.progress;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.objectives.Block;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.Death;
import org.mcsr.aatool.data.objectives.ObjectiveInterface;
import org.mcsr.aatool.net.Uuid;

public class WorldState extends ProgressState {
  public static final WorldState EMPTY = new WorldState();

  public Map<Uuid, Contribution> players = new HashMap<>();

  public WorldState() {}

  public WorldState(NetworkState state) {
    // Copy co-op state
    for (NetworkContribution player : state.players) {
      // Individual progress
      Contribution contribution = new Contribution(player);
      this.players.put(contribution.player, contribution);

      // Combined advancements
      for (Map.Entry<String, Completion> advancement : contribution.advancements.entrySet()) {
        String key = advancement.getKey();
        Completion value = advancement.getValue();
        Completion first = this.advancements.get(key);
        if (first == null || value.before(first.timestamp)) this.advancements.put(key, value);
      }

      // Combined criteria
      for (Map.Entry<String, Completion> criterion : contribution.criteria.entrySet()) {
        String key = criterion.getKey();
        Completion value = criterion.getValue();
        Completion first = this.criteria.get(key);
        if (first == null || value.before(first.timestamp)) this.criteria.put(key, value);
      }

      // Combined stats
      copyStats(player.pickup, this.pickupCounts);
      copyStats(player.drop, this.dropCounts);
      copyStats(player.mine, this.mineCounts);
      copyStats(player.craft, this.craftCounts);
      copyStats(player.use, this.useCounts);
      copyStats(player.kill, this.killCounts);

      // Enchanted golden apple
      this.obtainedGodApple |= contribution.obtainedGodApple;
      // Lapis
      this.obtainedLapis |= contribution.obtainedLapis;
    }

    this.inGameTime = state.inGameTime;
    this.kilometersFlown = state.kilometersFlown;
    this.itemsEnchanted = state.itemsEnchanted;
  }

  private static void copyStats(Map<String, Integer> source, Map<String, Integer> destination) {
    for (Map.Entry<String, Integer> statistic : source.entrySet()) {
      String key = statistic.getKey();
      destination.put(key, destination.getOrDefault(key, 0) + statistic.getValue());
    }
  }

  @Override
  public Set<Completion> completionsOf(ObjectiveInterface objective) {
    // Compile a list of all players who have completed this objective
    Set<Completion> completionists = new HashSet<>();

    if (objective instanceof Advancement advancement) {
      for (Contribution player : this.players.values()) {
        Completion completion = player.advancements.get(advancement.getId());
        if (completion != null) completionists.add(completion);
      }
    } else if (objective instanceof Criterion criterion) {
      for (Contribution player : this.players.values()) {
        Completion completion = player.criteria.get(Criterion.key(criterion.owner.getId(), criterion.getId()));
        if (completion != null) completionists.add(completion);
      }
    } else if (objective instanceof Block block) {
      for (Map.Entry<Uuid, Contribution> player : this.players.entrySet()) {
        Uuid key = player.getKey();
        Contribution value = player.getValue();

        if (value.useCounts.containsKey(block.getId())) {
          completionists.add(new Completion(key, Instant.MIN));
        }

        if (!block.hasAlternateIds()) continue;

        for (String id : block.getAlternateIds()) {
          if (value.useCounts.containsKey(id)) {
            completionists.add(new Completion(key, Instant.MIN));
          }
        }
      }
    } else if (objective instanceof Death death) {
      if (this.deathMessages.contains(death.getId())) {
        completionists.add(new Completion(Tracker.getMainPlayer(), Instant.MIN));
      }
    }

    return completionists;
  }

  public final void syncDeathMessages() {
    // TODO: ActiveInstance

    for (Death death : Tracker.DEATHS.getAll().values()) {
      for (String message : death.messages) {}
    }
  }
}
