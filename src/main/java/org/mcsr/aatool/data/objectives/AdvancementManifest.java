package org.mcsr.aatool.data.objectives;

import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utils.Pair;

public class AdvancementManifest implements Manifest {
  public final Map<String, Advancement> allAdvancements;
  public final Map<String, Advancement> remainingAdvancements;
  public final Map<String, Set<Advancement>> groups;
  public final Map<Pair<String, String>, Criterion> allCriteria;
  public final Map<Pair<String, String>, Criterion> remainingCriteria;

  private int combinedCompletedCount;

  public AdvancementManifest() {}

  public final int getCombinedCompletedCount() { return this.combinedCompletedCount; }

  public final int getCount() {}

  public final boolean tryGet(String advId, /*out */Advancement advancement) {}

  public final boolean tryGet(String advId, String critId, /*out */Criterion criterion) {}

  public final boolean tryGet(String groupId, /*out */Set<Advancement> group) {}

  public final void clearObjectives() {}

  public void refreshObjectives() {}

  private void parseFile(String file) {}

  private void requireAdvancement(XmlNode node, Set<Advancement> group) {}

  public final void updateState(ProgressState progress) {}

  public final void refreshRemainingCriteria() {}
}
