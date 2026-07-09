package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.Tracker;

public class MonstersHunted extends SingleAdvancement {
  public static final List<String> SUPPORTED_VERSIONS = List.of("1.16.5", "1.16");

  private static final String ID = "minecraft:adventure/kill_all_mobs";

  public MonstersHunted() {
    this.name = "Monsters Hunted";
    this.acronym = "MH";
    this.objective = "Monsters";
    this.action = "Killed";
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }

  @Override
  public void loadObjectives() {
    Tracker.ADVANCEMENTS.refreshObjectives();
    this.requirement = Tracker.ADVANCEMENTS.tryGetAdvancement(ID).value;
  }
}
