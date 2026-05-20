package org.mcsr.aatool.data.speedrunning;

public class Run {
  public Version gameVersion;
  public TimeSpan inGameTime;
  public TimeSpan realTime;
  public DateTime date;
  public String runnerSrcId;
  public String runner;
  public String status;
  public String comment;
  public boolean verifiable;
  public int extraStat;

  public String validatedLink;
  public Version parsedVersion;

  public Run() {}

  public Run(String name, TimeSpan igt) {}

  public String getLink() {}
  public void setLink(String value) {}

  public static boolean tryParse(LeaderboardSheet sheet, int rowIndex, String gameVersion, /*out */Run pb) {}
}
