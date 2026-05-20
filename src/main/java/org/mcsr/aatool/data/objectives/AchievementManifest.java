package org.mcsr.aatool.data.objectives;

public class AchievementManifest extends AdvancementManifest {
  private Achievement root;

  public final Achievement getRoot() { return this.root; }

  @Override
  public void refreshObjectives() {}
}
