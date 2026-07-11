package org.mcsr.aatool.utilities;

import java.time.Duration;

public class MoonPhase {
  private static final long DAY_SECONDS = 1200;
  private static final Phase[] PHASES = Phase.values();

  private MoonPhase() {}

  public static boolean isDayTime(Duration igt) {
    return igt.toSeconds() / (DAY_SECONDS / 2) % 2 == 0;
  }

  public static boolean isNightTime(Duration igt) { return !isDayTime(igt); }

  public static Phase phaseOf(Duration igt) {
    return PHASES[(int) (igt.toSeconds() / DAY_SECONDS % PHASES.length)];
  }

  public static boolean spawnsBlackCats(Duration igt) {
    return isNightTime(igt) && phaseOf(igt) == Phase.FULL;
  }

  public static boolean spawnsSlimes(Duration igt) {
    return isNightTime(igt) && phaseOf(igt) != Phase.NEW;
  }

  public static int slimeSpawnPercentage(Duration igt) {
    return switch (phaseOf(igt)) {
      case FULL -> 100;
      case WANING_GIBBOUS -> 75;
      case WANING_QUARTER -> 50;
      case WANING_CRESCENT -> 25;
      case NEW -> 0;
      case WAXING_CRESCENT -> 25;
      case WAXING_QUARTER -> 50;
      case WAXING_GIBBOUS -> 75;
    };
  }

  public enum Phase {
    FULL,
    WANING_GIBBOUS,
    WANING_QUARTER,
    WANING_CRESCENT,
    NEW,
    WAXING_CRESCENT,
    WAXING_QUARTER,
    WAXING_GIBBOUS
  }
}
