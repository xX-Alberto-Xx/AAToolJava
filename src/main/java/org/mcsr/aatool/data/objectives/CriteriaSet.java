package org.mcsr.aatool.data.objectives;

import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.data.objectives.complex.ArmorTrims;
import org.mcsr.aatool.data.progress.Completion;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Pair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CriteriaSet {
  public final Map<String, Criterion> all = new HashMap<>();
  public final Map<Uuid, Integer> progress = new HashMap<>();
  public final Advancement owner;
  public final String goal;

  private Uuid closestToCompletion = Uuid.EMPTY;

  public CriteriaSet(JsonObject obj, Advancement owner) {
    this.owner = owner;
    this.goal = JsonUtils.getString(obj, "goal", "Completed");
    if (obj == null) return;

    switch (this.owner.id) {
      case ArmorTrims.ADVANCEMENT_ID, ArmorTrims.CATEGORY_ID -> {
        for (JsonElement criterionElem : obj.getAsJsonArray("children")) {
          ArmorTrimCriterion criterion = new ArmorTrimCriterion(criterionElem.getAsJsonObject(), owner);
          this.all.put(criterion.id, criterion);
        }
      }

      default -> {
        for (JsonElement criterionElem : obj.getAsJsonArray("children")) {
          Criterion criterion = new Criterion(criterionElem.getAsJsonObject(), owner);
          this.all.put(criterion.id, criterion);
        }
      }
    }
  }

  public final Uuid getClosestToCompletion() { return this.closestToCompletion; }

  public final boolean any() { return !this.all.isEmpty(); }
  public final int getCount() { return this.all.size(); }
  public final int getMostCompleted() { return this.numberCompletedBy(this.closestToCompletion); }

  public final boolean contains(String criterion) { return this.all.containsKey(criterion); }

  public final int numberCompletedBy(Uuid player) {
    Integer completed = Config.getTracking().manualChecklistMode.getValue()
                      ? !this.progress.isEmpty() ? this.progress.values().iterator().next() : null
                      : this.progress.get(player);

    return completed != null ? completed : 0;
  }

  public final int percentCompletedBy(Uuid player) {
    int count = this.getCount();
    return count == 0 ? 0 : this.numberCompletedBy(player) * 100 / count;
  }

  public final void updateStates(WorldState progress) {
    if (!this.any()) return;

    // Update all criteria in this group and count them
    this.progress.clear();

    for (Criterion criterion : this.all.values()) {
      criterion.updateState(progress);

      for (Completion completion : criterion.completions) {
        this.progress.put(completion.player, this.progress.getOrDefault(completion.player, 0) + 1);
      }
    }

    this.findPlayerWithMost(progress);
  }

  public final void findPlayerWithMost(WorldState progress) {
    if (!this.any()) return;

    Map.Entry<Uuid, Integer> mostCompleted = Map.entry(Uuid.EMPTY, 0);

    for (Map.Entry<Uuid, Integer> player : this.progress.entrySet()) {
      if (player.getValue() >= mostCompleted.getValue()) mostCompleted = player;
    }

    this.closestToCompletion = mostCompleted.getKey();

    if (this.closestToCompletion.equals(Uuid.EMPTY) && !progress.players.isEmpty()) {
      this.closestToCompletion = progress.players.keySet().iterator().next();
    }
  }

  public final void cloneCriteria(Map<Pair<String, String>, Criterion> dictionary) {
    // Copy criteria to passed list
    for (Map.Entry<String, Criterion> criterion : this.all.entrySet()) {
      dictionary.put(new Pair<>(this.owner.id, criterion.getKey()), criterion.getValue());
    }
  }
}
