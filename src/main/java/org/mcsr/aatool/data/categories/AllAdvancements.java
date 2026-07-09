package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.Objective;

public class AllAdvancements extends Category {
  public static final List<String> SUPPORTED_VERSIONS = List.of(
    "1.21.6", "1.21", "1.20.5", "1.20", "1.19", "1.18", "1.17",
    "1.16.5", "1.16", "1.15", "1.14", "1.13", "1.12"
  );

  public AllAdvancements() {
    this.name = "All Advancements";
    this.acronym = "AA";
    this.objective = "Advancements";
    this.action = "Complete";
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }

  @Override
  public Iterable<? extends Objective> getOverlayObjectives() {
    return Tracker.ADVANCEMENTS.allAdvancements.values();
  }

  @Override
  public String getDefaultVersion() { return "1.16"; }

  @Override
  public int getTargetCount() { return Tracker.ADVANCEMENTS.getCount(); }
  @Override
  public int getCompletedCount() { return Tracker.ADVANCEMENTS.getCombinedCompletedCount(); }

  @Override
  public void loadObjectives() {
    Tracker.ADVANCEMENTS.refreshObjectives();
    Tracker.COMPLEX_OBJECTIVES.refreshObjectives();
  }
}
