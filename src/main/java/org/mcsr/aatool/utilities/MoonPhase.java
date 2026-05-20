package org.mcsr.aatool.utilities;

public class MoonPhase {
  private MoonPhase() {}

  public static void test() {}

  public static boolean isDayTime(TimeSpan igt) {}

  public static boolean isNightTime(TimeSpan igt) {}

  public static Phase phaseOf(TimeSpan igt) {}

  public static boolean spawnsBlackCats(TimeSpan igt) {}

  public static boolean spawnsSlimes(TimeSpan igt) {}

  public static int slimeSpawnPercentage(TimeSpan igt) {}

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
