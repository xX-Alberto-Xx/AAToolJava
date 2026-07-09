package org.mcsr.aatool.data.objectives;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.data.progress.Completion;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.enums.FrameType;
import org.mcsr.aatool.enums.ProgressFilter;
import org.mcsr.aatool.net.Player;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonObject;

public abstract class Objective implements ObjectiveInterface {
  protected String id;
  protected String icon;
  protected String name;
  protected String shortName;

  protected boolean canBeManuallyChecked;
  protected boolean completionOverride;
  public boolean manuallyChecked;
  protected boolean partial;

  protected Set<Completion> completions = new HashSet<>();
  protected Completion firstCompletion = Completion.EMPTY;
  protected FrameType frame = FrameType.NORMAL;

  public Objective() {}

  public Objective(JsonObject obj) {
    if (obj == null) return;

    // Parse properties from JSON
    this.id = JsonUtils.getString(obj, "id", "");
    this.name = JsonUtils.getString(obj, "name", "");
    this.shortName = JsonUtils.getString(obj, "short_name", this.name);
    this.canBeManuallyChecked = JsonUtils.getBoolean(obj, "manual", this.canBeManuallyChecked);

    // Parse icon
    this.icon = JsonUtils.getString(obj, "icon", "");

    if (this.icon.isEmpty()) {
      this.icon = this.id.substring(this.id.lastIndexOf('/') + 1);
    }

    // Parse frame
    String frame = JsonUtils.getString(
      obj, "frame", JsonUtils.getString(obj, "type", FrameType.NORMAL.toString())
    );

    try { this.frame = FrameType.valueOf(frame.toUpperCase()); }
    catch (IllegalArgumentException ignored) {}
  }

  @Override
  public final String getId() { return this.id; }
  @Override
  public final String getIcon() { return this.icon; }
  @Override
  public final String getName() { return this.name; }
  @Override
  public final String getShortName() { return this.shortName; }

  @Override
  public abstract String getFullStatus();
  @Override
  public abstract String getTinyStatus();

  public final boolean canBeManuallyChecked() { return this.canBeManuallyChecked; }
  public final boolean hasCompletionOverride() { return this.completionOverride; }
  @Override
  public final boolean isPartial() { return this.partial; }

  public final Set<Completion> getCompletions() { return this.completions; }
  public final Completion getFirstCompletion() { return this.firstCompletion; }
  public final FrameType getFrame() { return this.frame; }

  public void toggleManualCheck() {
    this.manuallyChecked ^= true;

    if (Config.getTracking().filter.getValue() == ProgressFilter.COMBINED) { // TODO: Peer
      this.updateState(Tracker.getState());
      return;
    }

    Uuid player = Player.tryGetUuid(Config.getTracking().soloFilterName.getValue());
    this.updateState(Tracker.getState().players.get(player != null ? player : Uuid.EMPTY));
  }

  public boolean isComplete() { return !this.completions.isEmpty() || this.completionOverride; }

  @Override
  public final Uuid getFirstToComplete() { return this.firstCompletion.player; }
  @Override
  public final Instant getWhenFirstCompleted() { return this.firstCompletion.timestamp; }

  @Override
  public boolean isCompletedByAnyone() { return !this.completions.isEmpty(); }

  @Override
  public final boolean completedBy(Uuid player) {
    for (Completion completion : this.completions) {
      if (player.equals(completion.player)) return true;
    }

    return false;
  }

  @Override
  public final Instant whenCompletedBy(Uuid player) {
    for (Completion completion : this.completions) {
      if (player.equals(completion.player)) return completion.timestamp;
    }

    return Instant.MIN;
  }

  @Override
  public void updateState(ProgressState progress) {
    if (Tracker.isWorldChanged() || Tracker.isSavesFolderChanged() || !Tracker.isWorking()) {
      this.manuallyChecked = false;
    }

    if (progress != null) {
      this.completions = progress.completionsOf(this);
      this.firstCompletion = this.completions.stream()
        .min((e1, e2) -> e1.timestamp.compareTo(e2.timestamp))
        .orElse(Completion.EMPTY);
    } else {
      this.completions.clear();
      this.firstCompletion = Completion.EMPTY;
    }
  }
}
