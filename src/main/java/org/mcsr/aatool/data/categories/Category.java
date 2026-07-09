package org.mcsr.aatool.data.categories;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.configuration.TrackingConfig;
import org.mcsr.aatool.data.objectives.Objective;
import org.mcsr.aatool.utilities.SearchUtils;
import org.mcsr.aatool.utilities.Version;

public abstract class Category {
  protected String name;
  protected String acronym;
  protected String objective;
  protected String action;

  private String currentVersion;
  private String currentMajorVersion;

  public Category() {
    this.currentVersion = this.getDefaultVersion();

    Version current = Version.tryParse(this.currentVersion);
    if (current != null) this.currentMajorVersion = current.major + "." + current.minor;
  }

  public final String getName() { return this.name; }
  public final String getAcronym() { return this.acronym; }
  public final String getAction() { return this.action; }
  public final String getObjective() { return this.objective; }

  public final String getCurrentVersion() { return this.currentVersion; }
  public final String getCurrentMajorVersion() { return this.currentMajorVersion; }

  public String getViewName() { return this.name.toLowerCase().replace(' ', '_'); }

  public final String getLatestSupportedVersion() {
    return this.getSupportedVersions().iterator().next();
  }

  public String getDefaultVersion() { return this.getLatestSupportedVersion(); }

  public boolean isComplete() { return this.getCompletedCount() >= this.getTargetCount(); }

  public String getCompletionMessage() {
    return "All " + this.getTargetCount() + ' ' + this.objective + ' ' + this.action + '!';
  }

  public String getStatus() {
    return this.getCompletedCount() + " / " + this.getTargetCount()
         + ' ' + this.objective + ' ' + this.action;
  }

  public abstract Iterable<String> getSupportedVersions();
  public abstract Iterable<? extends Objective> getOverlayObjectives();
  public abstract int getTargetCount();
  public abstract int getCompletedCount();

  public final boolean trySetVersion(String version) {
    Version number = Version.tryParse(version);

    if (number != null) {
      version = number.isAfter(1, 20, 4) && number.isBefore(1, 21)
              ? "1.20.5" // Handle sub-versioning of 1.20 due to wolves and armadillos
              : number.isAfter(1, 16, 1) && number.isBefore(1, 17)
                ? "1.16.5" // Handle sub-versioning of 1.16 due to piglin brutes
                : number.build > 0
                  ? number.major + "." + number.minor + '.' + number.build
                  : number.major + "." + number.minor;
    }

    if (SearchUtils.contains(this.getSupportedVersions(), version)) {
      this.currentVersion = version;
      this.currentMajorVersion = number != null
                               ? number.major + "." + number.minor
                               : this.currentVersion;

      TrackingConfig trackingConfig = Config.getTracking();
      trackingConfig.gameVersion.set(this.currentVersion);
      trackingConfig.trySave();
      return true;
    }

    return false;
  }

  public abstract void loadObjectives();
  public void update() {}
  public final int getCompletionPercent() { return (int) (this.getCompletionRatio() * 100); }

  public final float getCompletionRatio() {
    int target = this.getTargetCount();
    return target >= 1 ? (float) Math.min(this.getCompletedCount(), target) / target : 0;
  }
}
