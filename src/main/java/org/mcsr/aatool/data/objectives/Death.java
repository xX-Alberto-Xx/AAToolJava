package org.mcsr.aatool.data.objectives;

import java.util.ArrayList;
import java.util.List;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Death extends Objective {
  private boolean doubleHeight;
  private float lightLevel;

  public Iterable<String> messages;

  public Death(JsonObject obj) {
    super(obj);
    this.doubleHeight = JsonUtils.getBoolean(obj, "double_height", false);
    this.lightLevel = JsonUtils.getFloat(obj, "light_level", 0);

    List<String> messagesList = new ArrayList<>();

    for (JsonElement messageElem : obj.getAsJsonArray("messages")) {
      messagesList.add(messageElem.getAsJsonPrimitive().getAsString());
    }

    this.messages = messagesList;
    this.canBeManuallyChecked = true;
  }

  public final boolean isDoubleHeight() { return this.doubleHeight; }
  public final float getLightLevel() { return this.lightLevel; }

  public final boolean glows() { return this.lightLevel > 0; }

  @Override
  public String getFullStatus() { return this.name; }
  @Override
  public String getTinyStatus() { return this.shortName; }

  @Override
  public boolean isComplete() { return super.isComplete() || this.manuallyChecked; }

  public final void clear() { this.manuallyChecked = false; }

  @Override
  public void toggleManualCheck() {
    super.toggleManualCheck();
    Tracker.DEATHS.updateTotal();
  }
}
