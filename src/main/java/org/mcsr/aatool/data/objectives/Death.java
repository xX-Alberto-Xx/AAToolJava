package org.mcsr.aatool.data.objectives;

public class Death extends Objective {
  private boolean doubleHeight;
  private float lightLevel;

  public Iterable<String> messages;

  public Death(XmlNode node) {}

  public final boolean isDoubleHeight() { return this.doubleHeight; }
  public final float getLightLevel() { return this.lightLevel; }

  public final boolean glows() {}

  @Override
  public String getFullStatus() {}
  @Override
  public String getTinyStatus() {}

  @Override
  public boolean isComplete() {}

  public final void clear() {}

  @Override
  public void toggleManualCheck() {}
}
