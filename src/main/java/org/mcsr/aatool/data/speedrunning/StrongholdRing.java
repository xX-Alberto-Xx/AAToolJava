package org.mcsr.aatool.data.speedrunning;

import java.util.List;

public class StrongholdRing {
  public static final int THICKNESS;

  public final int totalStrongholdCount;
  public final int filledPortalCount;
  public final int startDistance;
  public final int endDistance;

  private Point referenceStronghold;
  private Point[] blindEstimates;
  private List<Point> filledPortals;
  private float angleBetweenStrongholds;
  private float angleOffset;

  public StrongholdRing(int strongholdCount, int startDistance) {}

  public final Point getReferenceStronghold() { return this.referenceStronghold; }
  public final Point[] getBlindEstimates() { return this.blindEstimates; }
  public final List<Point> getFilledPortals() { return this.filledPortals; }
  public final float getAngleBetweenStrongholds() { return this.angleBetweenStrongholds; }
  public final float getAngleOffset() { return this.angleOffset; }

  public final int getOptimalBlindDistance() {}

  public final int getDiameter() {}

  public final void fillPortal(Point coords) {}

  public final void setReferenceStronghold(Point coords) {}

  private void calculateOptimalBlindCoordinates() {}

  public final void clearProgress() {}

  private static float angle(Point start, Point end) {}

  private Point estimatedBlindCoordinates(double angle) {}
}
