package org.mcsr.aatool.data.objectives;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.data.categories.AllSmithingTemplates;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Result;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AdvancementManifest implements Manifest {
  public final Map<String, Advancement> allAdvancements = new HashMap<>();
  public final Map<String, Advancement> remainingAdvancements = new HashMap<>();
  public final Map<String, Set<Advancement>> groups = new HashMap<>();
  public final Map<Pair<String, String>, Criterion> allCriteria = new HashMap<>();
  public final Map<Pair<String, String>, Criterion> remainingCriteria = new HashMap<>();

  private int combinedCompletedCount;

  public final int getCombinedCompletedCount() { return this.combinedCompletedCount; }

  public final int getCount() { return this.allAdvancements.size(); }

  public final Result<Advancement> tryGetAdvancement(String advId) {
    return new Result<>(this.allAdvancements.containsKey(advId), this.allAdvancements.get(advId));
  }

  public final Result<Criterion> tryGetCriterion(String advId, String critId) {
    Pair<String, String> key = new Pair<>(advId, critId);
    return new Result<>(this.allCriteria.containsKey(key), this.allCriteria.get(key));
  }

  public final Result<Set<Advancement>> tryGetGroup(String groupId) {
    return new Result<>(this.groups.containsKey(groupId), this.groups.get(groupId));
  }

  @Override
  public final void clearObjectives() {
    this.groups.clear();
    this.allAdvancements.clear();
    this.remainingAdvancements.clear();
    this.allCriteria.clear();
    this.remainingCriteria.clear();
    this.combinedCompletedCount = 0;
  }

  @Override
  public void refreshObjectives() {
    this.clearObjectives();
    if (Tracker.getCategory() instanceof AllAchievements) return;

    if (Tracker.getCategory() instanceof AllSmithingTemplates) {
      this.parseFile(Paths.System.getArmorTrimsFile());
      return;
    }

    // Try to get list of all advancements' objective files
    Stream<Path> files = Paths.tryGetAllFiles(Paths.System.getAdvancementsFolder(), "*.json", false);

    if (files != null) {
      // Iterate advancement objective files for current game version
      try (files) { files.forEach(this::parseFile); }
    }
  }

  private void parseFile(Path file) {
    JsonArray advancements = JsonUtils.tryParseFile(file, JsonArray.class);
    if (advancements == null) return;

    // Add advancement group
    Set<Advancement> group = new HashSet<>();

    for (JsonElement advancementElem : advancements) {
      this.requireAdvancement(advancementElem.getAsJsonObject(), group);
    }

    this.groups.put(Paths.getFileNameWithoutExtension(file), group);
  }

  private void requireAdvancement(JsonObject obj, Set<Advancement> group) {
    Advancement advancement = new Advancement(obj);
    this.allAdvancements.put(advancement.id, advancement);
    group.add(advancement);

    if (advancement.hasCriteria()) {
      for (Map.Entry<String, Criterion> criterion : advancement.getCriteria().all.entrySet()) {
        this.allCriteria.put(new Pair<>(advancement.id, criterion.getKey()), criterion.getValue());
      }
    }
  }

  @Override
  public final void updateState(ProgressState progress) {
    this.remainingAdvancements.clear();
    this.combinedCompletedCount = 0;

    for (Map.Entry<String, Advancement> advancement : this.allAdvancements.entrySet()) {
      // Update advancement and completion count
      Advancement value = advancement.getValue();
      value.updateState(progress);

      if (value.isComplete()) this.combinedCompletedCount++;
      else this.remainingAdvancements.put(advancement.getKey(), value);
    }

    this.refreshRemainingCriteria();
  }

  public final void refreshRemainingCriteria() {
    this.remainingCriteria.clear();

    // Update global remaining criteria for overlay
    for (Map.Entry<Pair<String, String>, Criterion> criterion : this.allCriteria.entrySet()) {
      Criterion value = criterion.getValue();

      if (!value.owner.isComplete() && !value.completedByDesignated()) {
        this.remainingCriteria.put(criterion.getKey(), value);
      }
    }
  }
}
