package org.mcsr.aatool.data.objectives.complex;

import java.util.List;
import java.util.Map;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class SculkBlocks extends ComplexObjective {
  public static final Map<String, String> ALL_SCULK_BLOCKS = Map.of(
    "minecraft:sculk", "Sculk\0Block",
    "minecraft:sculk_catalyst", "Catalyst",
    "minecraft:sculk_shrieker", "Shrieker",
    "minecraft:sculk_sensor", "Sensor",
    "minecraft:sculk_vein", "Sculk\0Vein"
  );

  public List<String> remaining;
  public List<String> obtained;
  public List<String> placed;

  private boolean areAllObtained() { return this.obtained.size() >= ALL_SCULK_BLOCKS.size(); }
  private boolean areAllPlaced() { return this.placed.size() >= ALL_SCULK_BLOCKS.size(); }
  private boolean isOnLastBlock() { return this.remaining.size() == 1; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.clearAdvancedState();

    for (String block : ALL_SCULK_BLOCKS.keySet()) {
      if (progress.wasUsed(block)) {
        this.placed.add(block);
        this.remaining.remove(block);
      }

      if (progress.wasPickedUp(block)) {
        this.obtained.add(block);
        this.remaining.remove(block);
      }
    }

    this.completionOverride = this.areAllObtained() || this.areAllPlaced();
  }

  @Override
  protected void clearAdvancedState() {
    this.remaining.clear();
    this.remaining.addAll(ALL_SCULK_BLOCKS.keySet());
    this.obtained.clear();
    this.placed.clear();
  }

  @Override
  protected String getShortStatus() { return this.obtained + "\0/\0" + ALL_SCULK_BLOCKS.size(); }

  @Override
  protected String getLongStatus() {
    return this.areAllPlaced() ? "All\0Sculk\nPlaced"
         : this.areAllObtained() ? "Obtained\nAll\0Sculk"
         : this.isOnLastBlock() ? "Still\0Needs\n" + ALL_SCULK_BLOCKS.get(this.remaining.get(0))
         : "Obtain\nSculk";
  }

  @Override
  protected String getCurrentIcon() {
    if (this.isOnLastBlock()) {
      String last = this.remaining.get(0);
      last = last.substring(last.lastIndexOf(':') + 1);

      return "sculk_catalyst".equals(last) || "sculk_sensor".equals(last)
             ? last + "_block"
             : last;
    }

    return "sculk_shrieker";
  }
}
