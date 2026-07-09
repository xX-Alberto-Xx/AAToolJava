package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.data.progress.ProgressState;

public class Pickup extends ComplexObjective {
  protected int obtained;
  private final int required;

  public Pickup(String id) { this(id, null, 1); }
  public Pickup(String id, String name) { this(id, name, 1); }
  public Pickup(String id, String name, int required) {
    this.id = id;
    this.icon = id.replace("minecraft:", "");
    this.name = this.shortName = name != null ? name : "";
    this.required = required;
  }

  public final int getObtained() { return this.obtained; }
  public int getRequired() { return this.required; }

  @Override
  public String getFullStatus() { return this.getLongStatus(); }
  @Override
  public String getTinyStatus() { return this.getShortStatus(); }

  protected int getCount(ProgressState progress) {
    return Math.max(
      progress.timesPickedUp(this.id) +
      progress.timesCrafted(this.id) -
      progress.timesDropped(this.id) -
      progress.timesUsed(this.id),
      0
    );
  }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.obtained = this.getCount(progress);
    this.completionOverride = this.obtained > 0 && this.obtained >= this.required;
  }

  @Override
  protected void clearAdvancedState() { this.obtained = 0; }

  @Override
  protected String getShortStatus() { return this.getStatus(); }

  @Override
  protected String getLongStatus() { return this.getStatus(); }

  private String getStatus() {
    return "Estimated".equals(this.name) && this.obtained == 0
           ? "0"
           : this.name.contains(" ") ? this.name : this.obtained + "\0" + this.name;
  }
}
