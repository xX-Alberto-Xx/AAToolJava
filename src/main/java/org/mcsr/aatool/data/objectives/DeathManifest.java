package org.mcsr.aatool.data.objectives;

import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Result;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DeathManifest implements Manifest {
  private Map<String, Death> all = new HashMap<>();
  private int totalExperienced;

  public final Map<String, Death> getAll() { return this.all; }
  public final int getTotalExperienced() { return this.totalExperienced; }
  public final int getCount() { return this.all.size(); }

  public final Result<Death> tryGet(String id) {
    return new Result<>(this.all.containsKey(id), this.all.get(id));
  }

  @Override
  public final void clearObjectives() { this.all.clear(); }

  @Override
  public final void refreshObjectives() {
    this.clearObjectives();

    JsonArray deaths = JsonUtils.tryParseFile(Paths.System.getDeathMessagesFile(), JsonArray.class);
    if (deaths == null) return;

    // Build list of items to count
    for (JsonElement deathElem : deaths) {
      JsonObject deathObj = deathElem.getAsJsonObject();
      this.all.put(JsonUtils.getString(deathObj, "id", ""), new Death(deathObj));
    }
  }

  @Override
  public final void updateState(ProgressState progress) {
    for (Death death : this.all.values()) death.updateState(progress);
    this.updateTotal();
  }

  public final void updateTotal() {
    this.totalExperienced = 0;

    for (Death death : this.all.values()) {
      // Update completion count
      if (death.isComplete()) this.totalExperienced++;
    }
  }
}
