package org.mcsr.aatool.saves;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.complex.ArmorTrims;
import org.mcsr.aatool.data.objectives.complex.EGap;
import org.mcsr.aatool.data.progress.Completion;
import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.net.Uuid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AdvancementsFolder extends JsonFolder {
	private static final DateTimeFormatter TIME_FORMAT =
    DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss Z", Locale.ROOT);

  private AdvancementCompletion tryGetCompletionOf(String advancement, JsonStream json) {
    JsonObject token = (JsonObject) json.get(advancement);
    if (token == null) return null;

    AdvancementCompletion completion = new AdvancementCompletion(json, token, advancement);

    for (Map.Entry<String, JsonElement> elem : token.getAsJsonObject("criteria").entrySet()) {
      try {
        completion.addCriterion(
          advancement,
          elem.getKey(),
          Instant.from(TIME_FORMAT.parse(elem.getValue().getAsJsonPrimitive().getAsString()))
        );
      } catch (DateTimeParseException ignored) {}
    }

    return completion;
  }

  @Override
  protected void update(JsonStream json, WorldState state, Contribution contribution) {
    for (Advancement advancement : Tracker.ADVANCEMENTS.allAdvancements.values()) {
      AdvancementCompletion progress = this.tryGetCompletionOf(advancement.getId(), json);
      if (progress == null) continue;

      if (progress.advancementDone) this.updateAdvancements(progress, state, contribution);
      if (advancement.hasCriteria()) this.updateCriteria(progress, state, contribution);
    }

    // Detect god apple from chest using Mojang banner recipe
    if (this.tryGetCompletionOf(EGap.BANNER_RECIPE, json) != null) {
      state.obtainedGodApple = true;
      contribution.obtainedGodApple = true;
    }

    // Detect collection of armor trims
    for (String recipe : ArmorTrims.RECIPES) {
      AdvancementCompletion trim = this.tryGetCompletionOf(recipe, json);

      if (trim != null) {
        state.recipes.put(recipe, new Completion(trim.player, trim.getTimestamp()));
      }
    }

    // Detect lapis from chest using lapis block recipe
    if (this.tryGetCompletionOf("minecraft:recipes/building_blocks/lapis_block", json) != null) {
      state.obtainedLapis = true;
    }
  }

  private void updateAdvancements(AdvancementCompletion advancement, WorldState state, Contribution contribution) {
    // Update individual player progress
    String id = advancement.id;

    if (contribution.advancements.containsKey(id)) {
      throw new IllegalStateException("Advancement already present: " + id);
    }

    Completion completion = new Completion(advancement.player, advancement.getTimestamp());
    contribution.advancements.put(id, completion);

    // Update combined progress
    Completion globalFirst = state.advancements.get(id);

    if (globalFirst == null || globalFirst.after(completion.timestamp)) {
      state.advancements.put(id, completion);
    }
  }

  private void updateCriteria(AdvancementCompletion advancement, WorldState state, Contribution contribution) {
    for (Map.Entry<String, Instant> criterion : advancement.criteriaTimestamps.entrySet()) {
      // Update individual player progress
      String key = criterion.getKey();

      if (contribution.criteria.containsKey(key)) {
        throw new IllegalStateException("Criterion already present: " + key);
      }

      Completion completion = new Completion(advancement.player, criterion.getValue());
      contribution.criteria.put(key, completion);

      // Update combined progress
      Completion globalFirst = state.criteria.get(key);

      if (globalFirst == null || globalFirst.after(completion.timestamp)) {
        state.criteria.put(key, completion);
      }
    }
  }

  private class AdvancementCompletion {
    public final Map<String, Instant> criteriaTimestamps = new HashMap<>();
    public final Uuid player;
    public final String id;
    public final boolean advancementDone;

    private Instant timestamp = Instant.MIN;

    private AdvancementCompletion(JsonStream json, JsonObject token, String advancement) {
      this.id = advancement;
      this.player = json.player;
      this.advancementDone = token != null && token.getAsJsonPrimitive("done").getAsBoolean();
    }

    public final Instant getTimestamp() { return this.timestamp; }

    public final void addCriterion(String adv, String crit, Instant completed) {
      this.criteriaTimestamps.put(Criterion.key(adv, crit), completed);
      if (completed.isAfter(this.timestamp)) this.timestamp = completed;
    }
  }
}
