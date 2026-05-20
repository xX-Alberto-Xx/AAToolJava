package org.mcsr.aatool.saves;

import java.util.Map;

import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;

public class StatisticsFolder extends JsonFolder {
  private static final double TICKS_PER_SECOND;

  public final TimeSpan getInGameTime(JsonStream json) {}

  public final int getKilometersFlown(JsonStream json) {}

  public final int getCustomStat(JsonStream json, String name) {}

  @Override
  protected void update(JsonStream json, WorldState state, Contribution contribution) {}

  private void updateGlobalStats(JsonStream json, WorldState state) {}

  private Map<String, Integer> getOldVersionCounts(String group, String json) {}
}
