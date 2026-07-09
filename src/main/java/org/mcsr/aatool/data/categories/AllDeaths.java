package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.Objective;

public class AllDeaths extends Category {
  public static final List<String> SUPPORTED_VERSIONS = List.of("1.17", "1.16");

  public AllDeaths() {
    this.name = "All Deaths";
    this.acronym = "AD";
    this.objective = "Deaths";
    this.action = "Experienced";

    // TODO: SpriteSheet
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }
  @Override
  public Iterable<? extends Objective> getOverlayObjectives() { return Tracker.DEATHS.getAll().values(); }

  @Override
  public int getTargetCount() { return Tracker.DEATHS.getCount(); }
  @Override
  public int getCompletedCount() { return Tracker.DEATHS.getTotalExperienced(); }

  @Override
  public void loadObjectives() {
    Tracker.DEATHS.refreshObjectives();
    Tracker.COMPLEX_OBJECTIVES.refreshObjectives();
  }
}
