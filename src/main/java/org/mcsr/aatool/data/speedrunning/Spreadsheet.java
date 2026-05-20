package org.mcsr.aatool.data.speedrunning;

import java.util.Map;

public abstract class Spreadsheet {
  private static Map<String, String[][]> allSheets;
  private static Map<String, String> allRaw;

  public static final char LINE_BREAK;
  public static final char DELIMITER;
  public static final int HEADER_HEIGHT;

  private String key;
  private String header;
  private Point topLeft;
  protected boolean isValid;

  protected Spreadsheet(String csv, String key, String header/* = null*/) {}

  public final String getKey() { return this.key; }
  public final String getHeader() { return this.header; }
  public final Point getTopLeft() { return this.topLeft; }
  public final boolean isValid() { return this.isValid; }

  public final String[][] getRows() {}
  public final String getRawCsv() {}

  protected final boolean tryGetRow(int row, /*out */String[] cells) {}

  protected final boolean tryGetCell(int row, int col, /*out */String value) {}

  protected final Point find(/*params */String[] targets) {}

  public final void saveToCache() {}
}
