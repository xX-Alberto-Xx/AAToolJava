package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;

public class Block extends Objective {
  public boolean highlighted;
  private boolean doubleHeight;
  private float lightLevel;
  private boolean pickedUp;
  private boolean obtained;
  private String searchTags;
  private String[] alternateIds;

  public Block(XmlNode node) {}

  public final boolean isDoubleHeight() { return this.doubleHeight; }
  public final float getLightLevel() { return this.lightLevel; }
  public final boolean isPickedUp() { return this.pickedUp; }
  public final boolean isObtained() { return this.obtained; }
  public final String getSearchTags() { return this.searchTags; }
  public final String[] getAlternateIds() { return this.alternateIds; }

  public final boolean hasAlternateIds() {}

  public final boolean glows() {}

  @Override
  public String getFullStatus() {}
  @Override
  public String getTinyStatus() {}

  public final boolean hasBeenPlaced() {}

  @Override
  public boolean isCompletedByAnyone() {}

  public final void toggleHighlight() {}

  @Override
  public boolean isComplete() {}

  @Override
  public void updateState(ProgressState progress) {}
}
