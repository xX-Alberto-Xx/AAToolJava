package org.mcsr.aatool.data.speedrunning;

import java.time.Duration;
import java.time.LocalDate;

import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Version;

public class HardcoreStreak extends Run {
  public String runs;
  public String deaths;
  public int bestStreak;
  public boolean onBestStreak;

  public static HardcoreStreak tryParse(LeaderboardSheet sheet, int rowIndex, String gameVersion) {
    // Required columns
    String runner = sheet.tryGetRunner(rowIndex);
    if (runner.isEmpty()) return null;

    HardcoreStreak pb = new HardcoreStreak();
    pb.runner = runner;

    // Optional columns
    pb.gameVersion = Version.tryParse(gameVersion);

    Duration igt = sheet.tryGetIgt(rowIndex);
    pb.inGameTime = igt != null ? igt : Duration.ZERO;

    LocalDate date = sheet.tryGetDate(rowIndex);
    if (date == null) date = LocalDate.MIN;
    pb.date = date;

    String link = sheet.tryGetLink(rowIndex);
    pb.setLink(!link.isEmpty() ? link : Leaderboard.getAALinks().get(new Pair<>(runner, date)));
    pb.runs = sheet.tryGetRuns(rowIndex);
    pb.deaths = sheet.tryGetDeaths(rowIndex);

    Integer streak = sheet.tryGetStreak(rowIndex);
    pb.bestStreak = streak != null ? streak : 0;
    pb.onBestStreak = Boolean.TRUE.equals(sheet.tryGetOnBestStreak(rowIndex));
    return pb;
  }
}
