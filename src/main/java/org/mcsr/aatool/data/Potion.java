package org.mcsr.aatool.data;

import java.util.List;

public class Potion {
  private String name;
  private String icon;
  private List<String> ingredients;

  public Potion(XmlNode node) {}

  public final String getName() { return this.name; }
  public final String getIcon() { return this.icon; }
  public final List<String> getIngredients() { return this.ingredients; }
}
