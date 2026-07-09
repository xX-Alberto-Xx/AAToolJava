package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Strings;

import com.google.gson.JsonObject;

public class Criterion extends Objective {
  public final Advancement owner;

  public Criterion(JsonObject obj, Advancement advancement) {
    super(obj);
    this.owner = advancement;
    this.id = JsonUtils.getString(obj, "id", "");
    this.name = JsonUtils.getString(obj, "name", "");

    // Construct name from id if not explicitly provided
    String implicitName = this.id.substring(this.id.lastIndexOf(':') + 1);

    if (Strings.isNullOrEmpty(this.name)) {
      this.name = Strings.toTitleCase(implicitName.replace('_', ' '));
    }

    this.shortName = JsonUtils.getString(obj, "short_name", this.name);

    // Construct icon from id if not explicitly provided
    this.icon = JsonUtils.getString(obj, "icon", "");
    if (this.icon.isEmpty()) this.icon = implicitName.toLowerCase().replace(' ', '_');
  }

  public static String key(String advancement, String criterion) {
    return advancement + ' ' + criterion;
  }

  public final Uuid getDesignatedPlayer() { return this.owner.designatedPlayer; }
  public final String getOwnerId() { return this.owner.id; }

  public boolean completedByDesignated() { return this.completedBy(this.getDesignatedPlayer()); }

  @Override
  public boolean isComplete() {
    return this.completedByDesignated() || (
      Config.getTracking().manualChecklistMode.getValue() &&
      this.isCompletedByAnyone()
    );
  }

  @Override
  public String getFullStatus() { return this.name; }
  @Override
  public String getTinyStatus() { return this.shortName; }
}
