package org.mcsr.aatool.data.progress;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.data.objectives.Criterion;
import org.mcsr.aatool.data.objectives.ObjectiveInterface;

public abstract class ProgressState {
  public Map<String, Completion> advancements = new HashMap<>();
  public Map<String, Completion> criteria = new HashMap<>();
  public Map<String, Completion> recipes = new HashMap<>();
  public Set<String> deathMessages = new HashSet<>();

  public Map<String, Integer> pickupCounts = new HashMap<>();
  public Map<String, Integer> dropCounts = new HashMap<>();
  public Map<String, Integer> mineCounts = new HashMap<>();
  public Map<String, Integer> craftCounts = new HashMap<>();
  public Map<String, Integer> useCounts = new HashMap<>();
  public Map<String, Integer> killCounts = new HashMap<>();

  public Duration inGameTime = Duration.ZERO;

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

  public abstract Set<Completion> completionsOf(ObjectiveInterface objective);

  public final boolean advancementCompleted(String id) { return this.advancements.containsKey(id); }

  public final boolean criterionCompleted(String advancement, String criterion) {
    return this.criteria.containsKey(Criterion.key(advancement, criterion));
  }

  public final boolean wasPickedUp(String name) { return this.pickupCounts.containsKey(name); }
  public final boolean wasDropped(String name) { return this.dropCounts.containsKey(name); }
  public final boolean wasMined(String name) { return this.mineCounts.containsKey(name); }
  public final boolean wasCrafted(String name) { return this.craftCounts.containsKey(name); }
  public final boolean wasUsed(String name) { return this.useCounts.containsKey(name); }
  public final boolean wasKilled(String name) { return this.killCounts.containsKey(name); }

  public final int timesPickedUp(String name) {
    Integer count = this.pickupCounts.get(name);
    return count != null ? count : 0;
  }

  public final int timesDropped(String name) {
    Integer count = this.dropCounts.get(name);
    return count != null ? count : 0;
  }

  public final int timesMined(String name) {
    Integer count = this.mineCounts.get(name);
    return count != null ? count : 0;
  }

  public final int timesCrafted(String name) {
    Integer count = this.craftCounts.get(name);
    return count != null ? count : 0;
  }

  public final int timesUsed(String name) {
    Integer count = this.useCounts.get(name);
    return count != null ? count : 0;
  }

  public final int timesKilled(String name) {
    Integer count = this.killCounts.get(name);
    return count != null ? count : 0;
  }
}
