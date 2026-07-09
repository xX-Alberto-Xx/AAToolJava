package org.mcsr.aatool.data.objectives;

import java.util.Map;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Pair;

import com.google.gson.JsonObject;

public class AchievementManifest extends AdvancementManifest {
  private Achievement root;

  public final Achievement getRoot() { return this.root; }

  @Override
  public void refreshObjectives() {
    this.clearObjectives();
    if (!(Tracker.getCategory() instanceof AllAchievements)) return;

    // Load lists of achievements to track for this version
    JsonObject obj = JsonUtils.tryParseFile(Paths.System.getAchievementsFile(), JsonObject.class);
    if (obj == null) return;

    // Recursively build achievement tree
    this.root = new Achievement(obj);
    this.root.getAllChildrenRecursive(this.allAdvancements);

    // Add sub-criteria
    for (Advancement advancement : this.allAdvancements.values()) {
      if (!advancement.hasCriteria()) continue;

      for (Map.Entry<String, Criterion> criterion : advancement.getCriteria().all.entrySet()) {
        this.allCriteria.put(new Pair<>(advancement.id, criterion.getKey()), criterion.getValue());
      }
    }
  }
}
