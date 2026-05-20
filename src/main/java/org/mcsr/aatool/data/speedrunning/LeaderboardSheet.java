package org.mcsr.aatool.data.speedrunning;

public class LeaderboardSheet extends Spreadsheet {
  private final int runnerCol;
  private final int datesCol;
  private final int rankCol;
  private final int igtCol;
  private final int rtaCol;
  private final int commentCol;
  private final int statuCol;
  private final int linkCol;
  private final int verifiableCol;
  private final int extraStatCol;
  private final int runsCol;
  private final int deathsCol;
  private final int streakCol;
  private final int onBestStreakCol;
  private final int isHardcoreCol;
  private final int noF3Col;
  private final int rangeCol;

  private LeaderboardSheet(String csv, String key, String header) {}

  public static boolean tryParse(String csv, String key, String header, /*out */LeaderboardSheet sheet) {}

  public final boolean tryGetIgt(int index, /*out */TimeSpan igt) {}

  public final boolean tryGetRta(int index, /*out */TimeSpan rta) {}

  public final boolean tryGetRunner(int index, /*out */String runner) {}

  public final boolean tryGetRank(int index, /*out */int rank) {}

  public final boolean tryGetDate(int index, /*out */DateTime date) {}

  public final boolean tryGetStatus(int index, /*out */String status) {}

  public final boolean tryGetLink(int index, /*out */String status) {}

  public final boolean tryGetComment(int index, /*out */String comment) {}

  public final boolean tryGetVerifiability(int index, /*out */boolean isVerifiable) {}

  public final boolean tryGetExtraStat(int index, /*out */int extraStat) {}

  public final boolean tryGetRuns(int index, /*out */String runs) {}

  public final boolean tryGetDeaths(int index, /*out */String deaths) {}

  public final boolean tryGetStreak(int index, /*out */int streak) {}

  public final boolean tryGetOnBestStreak(int index, /*out */boolean onBestStreak) {}

  public final boolean tryGetIsHardcore(int index, /*out */boolean isHardcore) {}

  public final boolean tryGetIsNoF3(int index, /*out */boolean isNoF3) {}

  public final boolean tryGetRange(int index, /*out */String range) {}

  private static boolean tryParseTimeSpan(String timeString, /*out */TimeSpan time) {}
}
