package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class GhastTears extends ComplexObjective {
  public static final String ADVANCEMENT_ID = "minecraft:end/respawn_dragon";
  public static final String TEAR_ID = "minecraft:ghast_tear";
  public static final String CRYSTAL_ID = "minecraft:end_crystal";

  private static final int REQUIRED_TEARS = 4;

  private int tears;
  private int crystals;
  private boolean dragonRespawned;

  public GhastTears() { this.icon = "uneasy_alliance"; }

  private boolean hasAllTears() { return this.tears > REQUIRED_TEARS; }
  private boolean hasAnyCrystals() { return this.crystals > 0; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.dragonRespawned = progress.advancementCompleted(ADVANCEMENT_ID);
    this.tears = progress.timesPickedUp(TEAR_ID) - progress.timesDropped(TEAR_ID);

    this.crystals = progress.timesCrafted(CRYSTAL_ID)
                  + progress.timesPickedUp(CRYSTAL_ID)
                  - progress.timesDropped(CRYSTAL_ID);

    this.completionOverride = this.dragonRespawned || this.hasAllTears() || this.hasAnyCrystals();
  }

  @Override
  protected String getCurrentIcon() {
    return this.dragonRespawned || this.hasAnyCrystals() ? "respawn_dragon" : "uneasy_alliance";
  }

  @Override
  protected void clearAdvancedState() {
    this.dragonRespawned = false;
    this.tears = 0;
    this.crystals = 0;
  }

  @Override
  protected String getShortStatus() {
    return this.dragonRespawned ? "Done"
         : (this.hasAnyCrystals() ? this.crystals : this.tears) + " / " + REQUIRED_TEARS;
  }

  @Override
  protected String getLongStatus() {
    return this.dragonRespawned ? "End\0Again\nCompleted"
         : "Tears:\0" + this.tears + "\nCrystals:\0" + this.crystals;
  }
}
