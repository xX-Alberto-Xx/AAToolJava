package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.data.objectives.Objective;
import org.mcsr.aatool.data.speedrunning.StrongholdRing;
import org.mcsr.aatool.utilities.Point;

public class AllPortals extends Category {
  public static final List<StrongholdRing> RINGS = List.of(
    new StrongholdRing(3, 1280),
    new StrongholdRing(6, 4352),
    new StrongholdRing(10, 7424),
    new StrongholdRing(15, 10496),
    new StrongholdRing(21, 13568),
    new StrongholdRing(28, 16640),
    new StrongholdRing(36, 19712),
    new StrongholdRing(9, 22783)
  );

  public static final List<String> SUPPORTED_VERSIONS = List.of("1.16");

  private static final int TOTAL_STRONGHOLDS = 128;

  private static final List<Point> TEST_VALUES = List.of(
    new Point(1700, 1200), new Point(11770, 6700)
  );

  private int filledPortals = 0;

  public AllPortals() {
    this.name = "All Portals";
    this.acronym = "AP";
    this.objective = "End Portals";
    this.action = "Filled";
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }
  @Override
  public Iterable<? extends Objective> getOverlayObjectives() { return null; }
  @Override
  public int getTargetCount() { return TOTAL_STRONGHOLDS; }
  @Override
  public int getCompletedCount() { return this.filledPortals; }

  @Override
  public void loadObjectives() {
    for (Point testValue : TEST_VALUES) this.fillPortal(testValue);
  }

  public final void fillPortal(Point coordinates) {
    this.closestRing(coordinates).fillPortal(coordinates);

    this.filledPortals = 0;
    for (StrongholdRing ring : RINGS) this.filledPortals += ring.filledPortalCount;
  }

  public final void clearProgress() {
    this.filledPortals = 0;
    for (StrongholdRing ring : RINGS) ring.clearProgress();
  }

  private StrongholdRing closestRing(Point coordinates) {
    double distanceFromOrigin = Math.hypot(coordinates.x, coordinates.y);
    double smallestDifference = Double.MAX_VALUE;
    StrongholdRing closest = RINGS.get(0);

    for (StrongholdRing ring : RINGS) {
      double ringDifference = Math.abs(distanceFromOrigin - ring.getOptimalBlindDistance());

      if (ringDifference < smallestDifference) {
        smallestDifference = ringDifference;
        closest = ring;
      }
    }

    return closest;
  }
}
