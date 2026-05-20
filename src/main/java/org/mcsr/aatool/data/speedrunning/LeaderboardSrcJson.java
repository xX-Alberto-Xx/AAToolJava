package org.mcsr.aatool.data.speedrunning;

import java.util.List;

public class LeaderboardSrcJson {
  public List<Run> runs;

  private LeaderboardSrcJson(List<Run> runs) {}

  public static boolean tryParse(String json, String version, /*out */LeaderboardSrcJson parsed) {}

  private static double parseTimeString(String value) {}
}
