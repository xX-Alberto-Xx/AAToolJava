package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.Tracker;

public class AdventuringTime extends SingleAdvancement {
  public static final List<String> SUPPORTED_VERSIONS = List.of("1.20", "1.19", "1.18", "1.16");

  private static final String ID = "minecraft:adventure/adventuring_time";

  public AdventuringTime() {
    this.name = "Adventuring Time";
    this.acronym = "AT";
    this.objective = "Biomes";
    this.action = "Visited";
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }

  @Override
  public void loadObjectives() {
    Tracker.ADVANCEMENTS.refreshObjectives();
    this.requirement = Tracker.ADVANCEMENTS.tryGetAdvancement(ID).value;
  }
}
