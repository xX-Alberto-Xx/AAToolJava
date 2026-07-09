package org.mcsr.aatool.data.speedrunning;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Strings;
import org.mcsr.aatool.utilities.Version;

public class Run {
  private static final String SPEEDRUN_DOT_COM = "https://www.speedrun.com/";
  private static final String YOUTUBE_FULL = "https://www.youtube.com/";
  private static final String YOUTUBE_SHORT = "https://youtu.be/";
  private static final String TWITCH = "https://www.twitch.tv/";
  private static final Set<String> OPTIONAL_IGT_DATE_CATEGORIES = Set.of("1k no reset", "hardcore no reset");

  public Version gameVersion;
  public Duration inGameTime = Duration.ZERO;
  public Duration realTime = Duration.ZERO;
  public LocalDate date = LocalDate.MIN;
  public String runnerSrcId;
  public String runner;
  public String status;
  public String comment;
  public boolean verifiable;
  public int extraStat;

  private String validatedLink;

  public Run() {}

  public Run(String name, Duration igt) {
    this.runner = name;
    this.inGameTime = igt;
  }

  public String getLink() { return this.validatedLink; }

  public void setLink(String value) {
    if (!Strings.isNullOrEmpty(value) && (
      value.startsWith(SPEEDRUN_DOT_COM) ||
      value.startsWith(YOUTUBE_FULL) ||
      value.startsWith(YOUTUBE_SHORT) ||
      value.startsWith(TWITCH)
    )) {
      this.validatedLink = value;
    }
  }

  public static Run tryParse(LeaderboardSheet sheet, int rowIndex, String gameVersion) {
    // Required columns
    String runner = sheet.tryGetRunner(rowIndex);
    if (runner.isEmpty()) return null;

    Duration igt = sheet.tryGetIgt(rowIndex);

    if (igt == null) {
      if (!OPTIONAL_IGT_DATE_CATEGORIES.contains(sheet.getHeader())) return null;

      igt = Duration.ZERO;
    }

    LocalDate date = sheet.tryGetDate(rowIndex);

    if (date == null) {
      if (!OPTIONAL_IGT_DATE_CATEGORIES.contains(sheet.getHeader())) return null;

      date = LocalDate.MIN;
    }

    Run pb = new Run();
    pb.inGameTime = igt;
    pb.date = date;
    pb.runner = runner;

    // Optional columns
    pb.gameVersion = Version.tryParse(gameVersion);

    Duration rta = sheet.tryGetRta(rowIndex);
    pb.realTime = rta != null ? rta : Duration.ZERO;
    pb.runnerSrcId = "";
    pb.verifiable = Boolean.TRUE.equals(sheet.tryGetVerifiability(rowIndex));
    pb.status = sheet.tryGetStatus(rowIndex);
    pb.comment = sheet.tryGetComment(rowIndex);

    String link = sheet.tryGetLink(rowIndex);
    pb.setLink(!link.isEmpty() ? link : Leaderboard.getAALinks().get(new Pair<>(runner, date)));

    Integer extraStat = sheet.tryGetExtraStat(rowIndex);
    pb.extraStat = extraStat != null ? extraStat : 0;
    return pb;
  }
}
