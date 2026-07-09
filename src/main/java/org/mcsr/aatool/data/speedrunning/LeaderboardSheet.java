package org.mcsr.aatool.data.speedrunning;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import org.mcsr.aatool.utilities.Strings;

public class LeaderboardSheet extends Spreadsheet {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ROOT);

  private final int runnerCol;
  private final int datesCol;
  private final int rankCol;
  private final int igtCol;
  private final int rtaCol;
  private final int commentCol;
  private final int statusCol;
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

  private LeaderboardSheet(String csv, String key, String header) {
    super(csv, key, header);

    // Find column headers
    this.rankCol = this.find("#", "place", "rank").x;
    this.runnerCol = this.find("runner", "player", "name").x;
    this.igtCol = this.find("igt", "ingametime", "average igt").x;
    this.rtaCol = this.find("rta", "realtime").x;
    this.datesCol = this.find("date").x;
    this.commentCol = this.find("comments", "comment", "notes", "note").x;
    this.statusCol = this.find("status").x;
    this.linkCol = this.find("link").x;
    this.verifiableCol = this.find("verifiable").x;
    this.extraStatCol = this.find("blocks placed", "blocks", "total runs").x;
    this.runsCol = this.find("total runs").x;
    this.deathsCol = this.find("deaths").x;
    this.streakCol = this.find("best streak").x;
    this.onBestStreakCol = this.find("on best streak").x;
    this.isHardcoreCol = this.find("hardcore").x;
    this.noF3Col = this.find("no f3").x;
    this.rangeCol = this.find("range").x;

    this.isValid = this.runnerCol >= 0 && this.datesCol >= 0 && (
      this.igtCol >= 0 || "1k no reset".equals(header) || "all versions".equals(header)
    );
  }

  public static LeaderboardSheet tryParse(String csv, String key, String header) {
    LeaderboardSheet sheet = new LeaderboardSheet(csv, key, header);
    return sheet.isValid ? sheet : null;
  }

  public final Duration tryGetIgt(int index) {
    String timeString = this.tryGetCell(index, this.igtCol);
    return !timeString.isEmpty() ? tryParseDuration(timeString) : null;
  }

  public final Duration tryGetRta(int index) {
    String timeString = this.tryGetCell(index, this.rtaCol);
    return !timeString.isEmpty() ? tryParseDuration(timeString) : null;
  }

  public final String tryGetRunner(int index) {
    return this.tryGetCell(index, this.runnerCol);
  }

  public final Integer tryGetRank(int index) {
    return this.tryGetInt(index, this.rankCol);
  }

  public final LocalDate tryGetDate(int index) {
    try {
      return LocalDate.parse(this.tryGetCell(index, this.datesCol), DATE_FORMAT);
    } catch (DateTimeParseException ignored) {
      return null;
    }
  }

  public final String tryGetStatus(int index) {
    return this.tryGetCell(index, this.statusCol);
  }

  public final String tryGetLink(int index) {
    return this.tryGetCell(index, this.linkCol);
  }

  public final String tryGetComment(int index) {
    return this.tryGetCell(index, this.commentCol);
  }

  public final Boolean tryGetVerifiability(int index) {
    return this.tryGetBoolean(index, this.verifiableCol);
  }

  public final Integer tryGetExtraStat(int index) {
    return this.tryGetInt(index, this.extraStatCol);
  }

  public final String tryGetRuns(int index) {
    return this.tryGetCell(index, this.runsCol);
  }

  public final String tryGetDeaths(int index) {
    return this.tryGetCell(index, this.deathsCol);
  }

  public final Integer tryGetStreak(int index) {
    return this.tryGetInt(index, this.streakCol);
  }

  public final Boolean tryGetOnBestStreak(int index) {
    return this.tryGetBoolean(index, this.onBestStreakCol);
  }

  public final Boolean tryGetIsHardcore(int index) {
    return this.tryGetBoolean(index, this.isHardcoreCol);
  }

  public final Boolean tryGetIsNoF3(int index) {
    return this.tryGetBoolean(index, this.noF3Col);
  }

  public final String tryGetRange(int index) {
    return this.tryGetCell(index, this.rangeCol);
  }

  private Integer tryGetInt(int index, int col) {
    try { return Integer.valueOf(this.tryGetCell(index, col)); }
    catch (NumberFormatException ignored) { return null; }
  }

  private static Duration tryParseDuration(String timeString) {
    if (Strings.isNullOrEmpty(timeString)) return null;

    String[] tokens = timeString.strip().split(":");
    int h = 0;
    int m = 0;
    int s = 0;

    try {
      if (tokens.length == 3) {
        h = Integer.parseInt(tokens[0]);
        m = Integer.parseInt(tokens[1]);
        s = Integer.parseInt(tokens[2]);
      } else if (tokens.length == 2) {
        m = Integer.parseInt(tokens[0]);
        s = Integer.parseInt(tokens[1]);
      }
    } catch (NumberFormatException ignored) {
      return null;
    }

    return Duration.ofSeconds((h * 60 + m) * 60 + s);
  }
}
