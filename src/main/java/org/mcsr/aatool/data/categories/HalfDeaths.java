package org.mcsr.aatool.data.categories;

import org.mcsr.aatool.Tracker;

public class HalfDeaths extends AllDeaths {
  public HalfDeaths() {
    this.name = "Half Deaths";
    this.acronym = "HD";
    this.objective = "Deaths";
    this.acronym = "Experienced";
  }

  @Override
  public int getTargetCount() { return (super.getTargetCount() + 1) / 2; }

  @Override
  public String getCompletionMessage() {
    return "Half (" + this.getTargetCount() + ") of All " + this.objective + ' ' + this.action + '!';
  }

  @Override
  public String getViewName() { return "all_deaths"; }

  @Override
  public void loadObjectives() {
    Tracker.DEATHS.refreshObjectives();
    Tracker.COMPLEX_OBJECTIVES.refreshObjectives();
  }
}
