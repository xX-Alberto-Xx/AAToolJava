package org.mcsr.aatool.utilities;

public class Point {
  public static final Point ZERO = new Point(0, 0);

  public final int x;
  public final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
