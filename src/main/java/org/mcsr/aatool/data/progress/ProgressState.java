package org.mcsr.aatool.data.progress;

import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.data.objectives.ObjectiveInterface;

public abstract class ProgressState {
  public Map<String, Completion> advancements;
  public Map<String, Completion> criteria;
  public Map<String, Completion> recipes;
  public Set<String> deathMessages;

  public Map<String, Integer> pickupCounts;
  public Map<String, Integer> dropCounts;
  public Map<String, Integer> mineCounts;
  public Map<String, Integer> craftCounts;
  public Map<String, Integer> useCounts;
  public Map<String, Integer> killCounts;

  public TimeSpan inGameTime;

  public double kilometersFlown;

  public boolean obtainedGodApple;
  public boolean obtainedLapis;

  public int deaths;
  public int damageTaken;
  public int damageDealt;
  public int jumps;
  public int sleeps;
  public int saveAndQuits;
  public int itemsEnchanted;

  public ProgressState() {}

  public abstract Set<Completion> completionsOf(ObjectiveInterface objective);

  public final boolean advancementCompleted(String id) {}

  public final boolean criterionCompleted(String advancement, String criterion) {}

  public final boolean wasPickedUp(String name) {}

  public final boolean wasDropped(String name) {}

  public final boolean wasMined(String name) {}

  public final boolean wasCrafted(String name) {}

  public final boolean wasUsed(String name) {}

  public final boolean wasKilled(String name) {}

  public final int timesPickedUp(String name) {}

  public final int timesDropped(String name) {}

  public final int timesMined(String name) {}

  public final int timesCrafted(String name) {}

  public final int timesUsed(String name) {}

  public final int timesKilled(String name) {}
}
