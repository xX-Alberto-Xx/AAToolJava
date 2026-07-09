package org.mcsr.aatool.data.objectives;

import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.data.objectives.complex.ArmorTrims;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Result;

public class ComplexObjectiveManifest implements Manifest {
  private Map<String, ComplexObjective> allByName = new HashMap<>();

  public ComplexObjectiveManifest() { this.refreshObjectives(); }

  public final Map<String, ComplexObjective> getAllByName() { return this.allByName; }

  public final Result<ComplexObjective> tryGet(String typeName) {
    String key = typeName != null ? typeName.toLowerCase() : "";
    return new Result<>(this.allByName.containsKey(key), this.allByName.get(key));
  }

  @Override
  public final void clearObjectives() { this.allByName.clear(); }

  @Override
  public final void refreshObjectives() {
    this.clearObjectives();

    for (String type : ComplexObjective.TYPES.keySet()) {
      ComplexObjective objective = ComplexObjective.tryCreateInstance(type);
      if (objective != null) this.allByName.put(type, objective);
    }

    // Misc items
    this.addPickup("minecraft:nether_wart", "Wart", 3);
    this.addPickup("minecraft:ghast_tear", "/\04\0Tears", 4);
    this.addPickup("minecraft:pufferfish", "/\02\0Puffers", 2);
    this.addPickup("minecraft:azure_bluet", "Azure Bluet", 1);
    this.addPickup("minecraft:rabbit_foot", "Rabbit's Foot", 1);
    this.addPickup("minecraft:fermented_spider_eye", "Fermented Eye", 1);
    this.addPickup("minecraft:pottery_sherd", "Pottery Sherd", 4);
    this.addPickup("minecraft:sniffer_egg", "Sniffer Eggs", 3);
  }

  private void addPickup(String id, String name, int required) {
    this.allByName.put(id, new Pickup(id, name, required));
  }

  @Override
  public final void updateState(ProgressState progress) {
    for (ComplexObjective objective : this.allByName.values()) {
      objective.updateState(progress);
    }
  }

  public final void updateDynamicIcons(Time time) {
    if (this.allByName.get(ArmorTrims.class.getSimpleName().toLowerCase()) instanceof ArmorTrims trims) {
      trims.updateDynamicIcon(time);
    }
  }
}
