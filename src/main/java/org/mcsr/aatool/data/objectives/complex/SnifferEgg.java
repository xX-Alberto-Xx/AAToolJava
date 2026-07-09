package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class SnifferEgg extends Pickup {
  public static final String ITEM_ID = "minecraft:sniffer_egg";

  public SnifferEgg() {
    super(ITEM_ID, "Eggs", 2);
    this.icon = "obtain_sniffer_egg";
  }

  @Override
  protected int getCount(ProgressState progress) {
    return Math.max(
      progress.timesPickedUp(this.id) -
      progress.timesDropped(this.id) -
      progress.timesMined(this.id),
      0
    );
  }

  @Override
  protected String getShortStatus() { return this.getStatus(); }

  @Override
  protected String getLongStatus() { return this.getStatus(); }

  private String getStatus() { return this.obtained + "\0/\0" + this.getRequired() + "\0Eggs"; }
}
