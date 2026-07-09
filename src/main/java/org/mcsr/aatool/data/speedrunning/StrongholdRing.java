package org.mcsr.aatool.data.speedrunning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mcsr.aatool.utilities.Point;

public class StrongholdRing {
  private static final int THICKNESS = 1536;

  public final int totalStrongholdCount;
  public final int filledPortalCount = 0;
  public final int startDistance;
  public final int endDistance;

  private Point[] blindEstimates;
  private List<Point> filledPortals = new ArrayList<>();
  private float angleBetweenStrongholds;
  private float angleOffset;

  public StrongholdRing(int strongholdCount, int startDistance) {
    this.blindEstimates = new Point[strongholdCount - 1];
    Arrays.fill(this.blindEstimates, Point.ZERO);

    this.totalStrongholdCount = strongholdCount;
    this.startDistance = startDistance;
    this.endDistance = startDistance + THICKNESS;
    this.angleBetweenStrongholds = (float) (2 * Math.PI) / strongholdCount;
  }

  public final Point[] getBlindEstimates() { return this.blindEstimates; }
  public final List<Point> getFilledPortals() { return this.filledPortals; }
  public final float getAngleBetweenStrongholds() { return this.angleBetweenStrongholds; }
  public final float getAngleOffset() { return this.angleOffset; }

  public final int getOptimalBlindDistance() { return this.startDistance + THICKNESS / 2; }

  public final int getDiameter() { return this.endDistance * 2; }

  public final void fillPortal(Point coords) {
    if (this.filledPortals.size() >= this.totalStrongholdCount) return;

    if (this.filledPortals.isEmpty()) this.setReferenceStronghold(coords);
    this.filledPortals.add(coords);
  }

  public final void setReferenceStronghold(Point coords) {
    this.angleOffset = (float) Math.atan2(coords.y, coords.x);
    this.calculateOptimalBlindCoordinates();
  }

  private void calculateOptimalBlindCoordinates() {
    for (int i = 0; i < this.blindEstimates.length; i++) {
      this.blindEstimates[i] = this.estimatedBlindCoordinates(
        this.angleOffset + (i + 1) * this.angleBetweenStrongholds
      );
    }
  }

  public final void clearProgress() {}

  private Point estimatedBlindCoordinates(double angle) {
    int optimalBlindDistance = this.getOptimalBlindDistance();

    return new Point(
      (int) Math.round(optimalBlindDistance * Math.cos(angle)),
      (int) Math.round(optimalBlindDistance * Math.sin(angle))
    );
  }
}
