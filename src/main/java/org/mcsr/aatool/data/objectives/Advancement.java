package org.mcsr.aatool.data.objectives;

import java.util.Objects;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonObject;

public class Advancement extends Objective {
  protected Uuid designatedPlayer = Uuid.EMPTY;
  protected boolean designationLinked;
  private CriteriaSet criteria;

  public final boolean hiddenWhenRelaxed;
  public final boolean hiddenWhenCompact;
  public final boolean usedInHalfPercent;

  public Advancement(JsonObject obj) {
    this.criteria = new CriteriaSet(obj != null ? obj.getAsJsonObject("criteria") : null, this);
    this.usedInHalfPercent = JsonUtils.getBoolean(obj, "half", true);

    String hideMode = JsonUtils.getString(obj, "hidden", "false").strip();

    if ("true".equalsIgnoreCase(hideMode)) {
      this.hiddenWhenRelaxed = true;
      this.hiddenWhenCompact = true;
    } else if ("false".equalsIgnoreCase(hideMode)) {
      this.hiddenWhenRelaxed = false;
      this.hiddenWhenCompact = false;
    } else {
      this.hiddenWhenRelaxed = "relaxed".equals(hideMode);
      this.hiddenWhenCompact = "compact".equals(hideMode);
    }

    this.parseCriteria(obj);

    if (this.hasCriteria()) {} // TODO: Peer

    this.linkDesignation();
  }

  public final Uuid designatedPlayer() { return this.designatedPlayer; }
  public final boolean isDesignationLinked() { return this.designationLinked; }
  public final CriteriaSet getCriteria() { return this.criteria; }

  @Override
  public String getFullStatus() { return this.name; }
  @Override
  public String getTinyStatus() { return this.shortName; }

  public final boolean hasCriteria() { return this.criteria.any(); }

  @Override
  public boolean isComplete() {
    return super.isComplete() || (
      Config.getTracking().manualChecklistMode.getValue() &&
      this.hasCriteria() &&
      this.criteria.numberCompletedBy(Uuid.EMPTY) >= this.criteria.getCount()
    );
  }

  public final void linkDesignation() { this.designationLinked = true; }
  public final void unlinkDesignation() { this.designationLinked = false; }

  public final void designate(Uuid id) {
    if (id.equals(Uuid.EMPTY)) return;

    if (!Objects.equals(id, this.designatedPlayer) || this.designationLinked) {
      this.designatedPlayer = id;
      Tracker.invalidateDesignations();

      // TODO: Server
    }
  }

  public final Uuid getDesignatedPlayer() {
    if (this.designationLinked) {} // TODO: Peer

    return !this.designatedPlayer.equals(Uuid.EMPTY)
           ? this.designatedPlayer
           : Tracker.getMainPlayer();
  }

  @Override
  public void updateState(ProgressState progress) {
    super.updateState(progress);
    this.criteria.updateStates(Tracker.getState());
    if (!this.hasCriteria()) return;

    // Handle auto-designation
    if (!(Tracker.isInvalidated() || Config.getTracking().isFilterChanged())) return; // TODO: Peer

    // TODO: Peer
  }

  protected final void parseCriteria(JsonObject advancementObj) {
    // Initialize criteria if this advancement has any
    JsonObject criteriaObj = advancementObj.getAsJsonObject("criteria");

    if (criteriaObj != null) {
      this.criteria = new CriteriaSet(criteriaObj, this);
      this.designate(Uuid.EMPTY);
    }
  }
}
