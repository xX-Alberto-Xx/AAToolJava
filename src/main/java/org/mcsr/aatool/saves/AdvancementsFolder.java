package org.mcsr.aatool.saves;

import java.util.Map;

import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utils.Pair;

public class AdvancementsFolder extends JsonFolder {
  private boolean tryGetCompletionOf(String advancement, JsonStream json, /*out */AdvancementCompletion completion) {}

  private Map<Pair<String, String>, DateTime> tryGetCompletionOf(Advancement advancement, JsonStream json) {}

  @Override
  protected void update(JsonStream json, WorldState state, Contribution contribution) {}

  private void updateAdvancements(AdvancementCompletion advancement, WorldState state, Contribution contribution) {}

  private void updateCriteria(AdvancementCompletion advancement, WorldState state, Contribution contribution) {}

  private class AdvancementCompletion {
    public final Map<String, DateTime> criteriaTimestamps;
    public final Uuid player;
    public final String id;
    public final boolean advancementDone;

    private DateTime timestamp;

    public AdvancementCompletion(JsonStream json, dynamic token, String advancement) {}

    public final DateTime getTimestamp() { return this.timestamp; }

    public final void addCriterion(String adv, String crit, DateTime completed) {}
  }
}
