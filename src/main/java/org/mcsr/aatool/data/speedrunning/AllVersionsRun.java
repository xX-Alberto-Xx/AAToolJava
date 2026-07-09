package org.mcsr.aatool.data.speedrunning;

import java.time.Duration;
import java.time.LocalDate;

import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Version;

public class AllVersionsRun extends Run {
  public String range;
  public boolean hardcore;
  public boolean noF3;

  public static AllVersionsRun tryParse(LeaderboardSheet sheet, int rowIndex, String gameVersion) {
    // Required columns
    String runner = sheet.tryGetRunner(rowIndex);
    if (runner.isEmpty()) return null;

    AllVersionsRun pb = new AllVersionsRun();
    pb.runner = runner;

    // Optional columns
    pb.gameVersion = Version.tryParse(gameVersion);

    Duration rta = sheet.tryGetRta(rowIndex);
    pb.realTime = rta != null ? rta : Duration.ZERO;

    LocalDate date = sheet.tryGetDate(rowIndex);
    if (date == null) date = LocalDate.MIN;
    pb.date = date;

    String link = sheet.tryGetLink(rowIndex);
    pb.setLink(!link.isEmpty() ? link : Leaderboard.getAALinks().get(new Pair<>(runner, date)));
    pb.range = sheet.tryGetRange(rowIndex);
    pb.noF3 = Boolean.TRUE.equals(sheet.tryGetIsNoF3(rowIndex));
    pb.hardcore = Boolean.TRUE.equals(sheet.tryGetIsHardcore(rowIndex));
    return pb;
  }
}
