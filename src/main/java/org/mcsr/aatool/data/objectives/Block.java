package org.mcsr.aatool.data.objectives;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mcsr.aatool.data.progress.Completion;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Block extends Objective {
  public boolean highlighted;
  private boolean doubleHeight;
  private float lightLevel;
  private boolean pickedUp;
  private boolean obtained;
  private String searchTags;
  private String[] alternateIds = {};

  public Block(JsonObject obj) {
    super(obj);
    this.id = "minecraft:" + obj.getAsJsonPrimitive("block_name").getAsString();
    this.doubleHeight = JsonUtils.getBoolean(obj, "double_height", false);
    this.lightLevel = JsonUtils.getFloat(obj, "light_level", 0);

    List<String> ids = new ArrayList<>();

    for (JsonElement idElem : obj.getAsJsonArray("alternate_ids")) {
      ids.add(idElem.getAsJsonPrimitive().getAsString());
    }

    this.alternateIds = ids.toArray(String[]::new);
  }

  public final boolean isDoubleHeight() { return this.doubleHeight; }
  public final float getLightLevel() { return this.lightLevel; }
  public final boolean isPickedUp() { return this.pickedUp; }
  public final boolean isObtained() { return this.obtained; }
  public final String getSearchTags() { return this.searchTags; }
  public final String[] getAlternateIds() { return this.alternateIds; }

  public final boolean hasAlternateIds() { return this.alternateIds.length > 0; }

  public final boolean glows() { return this.lightLevel > 0; }

  @Override
  public String getFullStatus() { return this.name; }
  @Override
  public String getTinyStatus() { return this.name; }

  public final boolean hasBeenPlaced() { return !this.firstCompletion.isEmpty(); }

  @Override
  public boolean isCompletedByAnyone() { return this.hasBeenPlaced() || this.manuallyChecked; }

  public final void toggleHighlight() { this.highlighted ^= true; }

  @Override
  public boolean isComplete() { return this.isCompletedByAnyone(); }

  @Override
  public void updateState(ProgressState progress) {
    if (progress == null) {
      this.firstCompletion = Completion.EMPTY;
      this.pickedUp = false;
      this.obtained = false;
      return;
    }

    this.pickedUp = progress.wasPickedUp(this.id);
    Set<Completion> placers = progress.completionsOf(this);

    if (placers.isEmpty()) {
      this.firstCompletion = Completion.EMPTY;
    } else if (this.firstCompletion.isEmpty()) {
      this.firstCompletion = placers.iterator().next();
      this.highlighted = false;
    }

    this.obtained = this.pickedUp || this.hasBeenPlaced();
  }
}
