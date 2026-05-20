package org.mcsr.aatool.data.speedrunning;

public class HardcoreStreak extends Run {
  public String runs;
  public String deaths;
  public int bestStreak;
  public boolean onBestStreak;

  public static boolean tryParse(LeaderboardSheet sheet, int rowIndex, String gameVersion, /*out */HardcoreStreak pb) {}
}
