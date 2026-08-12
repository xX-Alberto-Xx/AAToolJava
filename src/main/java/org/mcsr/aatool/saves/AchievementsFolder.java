package org.mcsr.aatool.saves;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.progress.Completion;
import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class AchievementsFolder extends JsonFolder {
  private static boolean isCompleted(String achievement, JsonStream json) {
    if (json == null) return false;

    JsonElement achievementElem = json.get(achievement);
    if (achievementElem == null) return false;

    if (achievementElem instanceof JsonPrimitive value) {
      try { return value.getAsInt() > 0; }
      catch (NumberFormatException ignored) {}
    }

    JsonPrimitive value = achievementElem.getAsJsonObject().getAsJsonPrimitive("value");
    if (value == null) return false;

    try { return value.getAsInt() > 0; }
    catch (NumberFormatException ignored) { return false; }
  }

  private static Set<String> getCompletedCriteria(Advancement advancement, JsonStream json) {
    Set<String> completed = new HashSet<>();
    if (json == null) return completed;

    JsonObject advancementObj = (JsonObject) json.get(advancement.getId());
    if (advancementObj == null) return completed;

    JsonArray criteriaList = advancementObj.getAsJsonArray("progress");
    if (criteriaList == null) return completed;

    // Advancement has criteria. Add them
    for (JsonElement criterionElem : criteriaList) {
      String criterion = criterionElem.getAsJsonPrimitive().getAsString();

      if (advancement.getCriteria().contains(criterion)) {
        completed.add(Criterion.key(advancement.getId(), criterion));
      }
    }

    return completed;
  }

  @Override
  protected void update(JsonStream json, WorldState state, Contribution contribution) {
    Completion completion = new Completion(json.player, Instant.MIN);

    for (Advancement achievement : Tracker.ACHIEVEMENTS.allAdvancements.values()) {
      String id = achievement.getId();

      if (isCompleted(id, json)) {
        state.advancements.put(id, completion);
        contribution.advancements.put(id, completion);
      }

      if (achievement.hasCriteria()) {
        for (String criterion : getCompletedCriteria(achievement, json)) {
          state.criteria.put(criterion, completion);
          contribution.criteria.put(criterion, completion);
        }
      }
    }
  }
}
