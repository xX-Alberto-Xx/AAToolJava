package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.objectives.Pickup;
import org.mcsr.aatool.data.progress.ProgressState;

public class NautilusShells extends Pickup {
  private static final String HDWGH = "minecraft:nether/all_effects";
  private static final String CONDUIT = "minecraft:conduit";

  private boolean conduitCrafted;
  private boolean conduitPlaced;
  private boolean hdwghComplete;

  public NautilusShells() {
    super("minecraft:nautilus_shell");
    this.name = this.getClass().getSimpleName();
  }

  @Override
  public int getRequired() { return 8; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    super.updateAdvancedState(progress);

    this.hdwghComplete = progress.advancementCompleted(HDWGH);
    this.conduitCrafted = progress.wasCrafted(CONDUIT);
    this.conduitPlaced = progress.wasUsed(CONDUIT);
    this.completionOverride |= this.conduitCrafted || this.conduitPlaced;

    if (Tracker.getCategory() instanceof AllBlocks) {
      this.partial = !this.conduitPlaced;
    } else {
      this.completionOverride |= this.hdwghComplete;
      this.partial = !this.hdwghComplete;
    }
  }

  @Override
  protected void clearAdvancedState() {
    super.clearAdvancedState();
    this.conduitCrafted = false;
    this.conduitPlaced = false;
    this.hdwghComplete = false;
  }

  @Override
  protected String getShortStatus() {
    return this.hdwghComplete ? "Done"
         : this.conduitPlaced ? "Ready"
         : this.obtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getLongStatus() {
    return !(Tracker.getCategory() instanceof AllBlocks) && this.hdwghComplete ? "HDWGH\nComplete"
         : this.conduitPlaced ? "Conduit\nPlaced"
         : this.conduitCrafted ? "Conduit\nCrafted"
         : "Shells\n" + this.obtained + "\0/\0" + this.getRequired();
  }

  @Override
  protected String getCurrentIcon() {
    if (Tracker.getCategory() instanceof AllBlocks) {
      if (this.conduitPlaced) return "conduit";
    } else {
      if (this.hdwghComplete) return "all_effects";
      if (this.conduitPlaced) return "conduit_placed";
    }

    return this.conduitCrafted ? "conduit" : "nautilus_shell";
  }
}
