package org.mcsr.aatool.data;

public class Cell {
  public int row;
  public int column;

  public Cell(int row, int column) {
    this.row = row;
    this.column = column;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Cell other && this.row == other.row && this.column == other.column;
  }

  @Override
  public int hashCode() { return (17 * 23 + this.row) * 23 + this.column; }
}
