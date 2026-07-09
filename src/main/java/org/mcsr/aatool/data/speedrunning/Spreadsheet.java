package org.mcsr.aatool.data.speedrunning;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.utilities.Point;
import org.mcsr.aatool.utilities.Strings;

public abstract class Spreadsheet {
  private static Map<String, String[][]> allSheets = new HashMap<>();
  private static Map<String, String> allRaw = new HashMap<>();

  public static final String LINE_BREAK = "\n";
  public static final String DELIMITER = ",";

  private String key;
  private String header;
  private Point topLeft = Point.ZERO;
  protected boolean isValid;

  protected Spreadsheet(String csv, String key) { this(csv, key, null); }
  protected Spreadsheet(String csv, String key, String header) {
    this.key = key;
    this.header = header;

    String[] rows = csv.split(LINE_BREAK);
    String[][] sheet = new String[rows.length][];
    for (int i = 0; i < rows.length; i++) sheet[i] = rows[i].split(DELIMITER);
    allSheets.put(key, sheet);
    allRaw.put(key, csv);

    // Find and lock onto a header anywhere on the sheet
    if (!Strings.isNullOrEmpty(header)) this.topLeft = this.find(header);
  }

  public final String getKey() { return this.key; }
  public final String getHeader() { return this.header; }
  public final Point getTopLeft() { return this.topLeft; }
  public final boolean isValid() { return this.isValid; }

  public final String[][] getRows() { return allSheets.get(this.key); }
  public final String getRawCsv() { return allRaw.get(this.key); }

  protected final String[] tryGetRow(int row) {
    int adjustedIndex = row + this.topLeft.y;
    if (adjustedIndex < 0) return null;

    String[][] rows = this.getRows();
    return adjustedIndex < rows.length ? rows[adjustedIndex] : null;
  }

  protected final String tryGetCell(int row, int col) {
    String value = "";

    if (col >= 0 && this.isValid) {
      String[] cells = this.tryGetRow(row);
      if (cells != null) value = cells[col];
    }

    return value;
  }

  protected final Boolean tryGetBoolean(int index, int col) {
    String cell = this.tryGetCell(index, col).strip();

    return "true".equalsIgnoreCase(cell) ? Boolean.TRUE
         : "false".equalsIgnoreCase(cell) ? Boolean.FALSE
         : null;
  }

  protected final Point find(String... targets) {
    int r = this.topLeft.y;
    if (r < 0) return new Point(-1, -1);

    String[][] rows = this.getRows();
    Set<String> targetsSet = Set.of(targets);

    for (; r < rows.length; r++) {
      String[] cells = rows[r];

      for (int c = this.topLeft.x; c < cells.length; c++) {
        if (targetsSet.contains(cells[c].strip().toLowerCase())) return new Point(c, r);
      }
    }

    return new Point(-1, -1);
  }

  public final void saveToCache() {
    try {
      // Cache leaderboard so it loads instantly next launch
      // Overwrite to keep leaderboard up to date
      Files.createDirectories(Paths.System.LEADERBOARDS_FOLDER);
      Files.writeString(Paths.System.leaderboardFile(this.key), this.getRawCsv());
    } catch (IOException ignored) {
      // Couldn't save file. Ignore and move on
    }
  }
}
