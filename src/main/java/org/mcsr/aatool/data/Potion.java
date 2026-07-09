package org.mcsr.aatool.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Potion {
  private String name;
  private String icon;
  private List<String> ingredients = new ArrayList<>();

  public Potion(JsonObject obj) {
    // Initialize members from JSON
    JsonPrimitive iconElement = obj.getAsJsonPrimitive("icon");
    if (iconElement != null) this.icon = iconElement.getAsString();

    JsonPrimitive nameElement = obj.getAsJsonPrimitive("name");
    if (nameElement != null) this.name = nameElement.getAsString();

    for (JsonElement ingredient : obj.getAsJsonArray("ingredients")) {
      this.ingredients.add(ingredient.getAsString());
    }
  }

  public final String getName() { return this.name; }
  public final String getIcon() { return this.icon; }
  public final List<String> getIngredients() { return this.ingredients; }
}
