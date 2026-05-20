package org.mcsr.aatool.saves;

import java.util.Set;

import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;

public class AchievementsFolder extends JsonFolder {
  private static boolean isCompleted(String achievement, JsonStream json) {}

  private Set<String> getCompletedCriteria(Advancement advancement, JsonStream json) {}

  @Override
  protected void update(JsonStream json, WorldState state, Contribution contribution) {}
}
