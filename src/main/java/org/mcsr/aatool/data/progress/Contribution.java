package org.mcsr.aatool.data.progress;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.objectives.Block;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.Death;
import org.mcsr.aatool.data.objectives.ObjectiveInterface;
import org.mcsr.aatool.net.Uuid;

public class Contribution extends ProgressState {
  public final Uuid player;

  public Contribution(Uuid player) { this.player = player; }

  public Contribution(NetworkContribution network) {
    this(network.uuid);

    // Add advancements
    for (Map.Entry<String, Instant> advancement : network.advancements.entrySet()) {
      this.advancements.put(
        advancement.getKey(),
        new Completion(this.player, advancement.getValue())
      );
    }

    // Add criteria
    for (NetworkCriteriaSet criteriaSet : network.multiparts) {
      for (String crit : criteriaSet.list) {
        this.criteria.put(
          Criterion.key(criteriaSet.advancement, crit),
          new Completion(this.player, Instant.MIN)
        );
      }
    }

    // Add stats
    this.pickupCounts.putAll(network.pickup);
    this.dropCounts.putAll(network.drop);
    this.mineCounts.putAll(network.mine);
    this.craftCounts.putAll(network.craft);
    this.useCounts.putAll(network.use);
    this.killCounts.putAll(network.kill);
  }

  @Override
  public Set<Completion> completionsOf(ObjectiveInterface objective) {
    // Compile a list of all players who have completed this objective
    Set<Completion> completionists = new HashSet<>();

    if (objective instanceof Advancement advancement) {
      Completion completion = this.advancements.get(advancement.getId());
      if (completion != null) completionists.add(completion);
    } else if (objective instanceof Criterion criterion) {
      Completion completion = this.criteria.get(Criterion.key(criterion.getOwnerId(), criterion.getId()));
      if (completion != null) completionists.add(completion);
    } else if (objective instanceof Block block) {
      if (this.wasUsed(block.getId())) {
        completionists.add(new Completion(this.player, Instant.MIN));
      }
    } else if (objective instanceof Death death) {
      if (this.deathMessages.contains(death.getId())) {
        completionists.add(new Completion(this.player, Instant.MIN));
      }
    }

    return completionists;
  }
}
