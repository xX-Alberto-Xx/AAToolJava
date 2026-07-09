package org.mcsr.aatool.data.objectives;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Achievement extends Advancement {
  public final Map<String, Achievement> children = new HashMap<>();
  public final Achievement parent;

  public Achievement(JsonObject obj) { this(obj, null); }
  public Achievement(JsonObject obj, Achievement parent) {
    super(obj);
    this.parent = parent;
    this.id = "achievement." + this.id;

    // Recursively build nested structure of pre-1.12 achievements
    for (JsonElement child : obj.getAsJsonArray("children")) {
      Achievement achievement = new Achievement(child.getAsJsonObject(), this);
      this.children.put(achievement.id, achievement);
    }

    this.parseCriteria(obj);
  }

  public final boolean isRoot() { return this.parent == null; }
  public final boolean isLocked() { return !this.isRoot() && !this.parent.isComplete(); }

  public final void getAllChildrenRecursive(Map<String, Advancement> children) {
    children.put(this.id, this);
    for (Achievement child : this.children.values()) child.getAllChildrenRecursive(children);
  }
}
