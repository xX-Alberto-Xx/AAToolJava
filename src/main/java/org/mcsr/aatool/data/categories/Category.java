package org.mcsr.aatool.data.categories;

import org.mcsr.aatool.data.objectives.Objective;

public abstract class Category {
  protected String name;
  protected String acronym;
  protected String action;
  protected String objective;

  private String currentVersion;
  private String currentMajorVersion;

  public Category() {}

  public final String getName() { return this.name; }
  public final String getAcronym() { return this.acronym; }
  public final String getAction() { return this.action; }
  public final String getObjective() { return this.objective; }

  public final String getCurrentVersion() { return this.currentVersion; }
  public final String getCurrentMajorVersion() { return this.currentMajorVersion; }

  public String getViewName() {}
  public final String getLatestSupportedVersion() {}

  public String getDefaultVersion() {}

  public boolean isComplete() {}

  public String getCompletionMessage() {}

  public String getStatus() {}

  public abstract Iterable<String> getSupportedVersions();
  public abstract Iterable<Objective> getOverlayObjectives();
  public abstract int getTargetCount();
  public abstract int getCompletedCount();

  public final boolean trySetVersion(String version) {}

  public abstract void loadObjectives();
  public void update() {}
  public final int getCompletionPercent() {}

  public final float getCompletionRatio() {}
}
