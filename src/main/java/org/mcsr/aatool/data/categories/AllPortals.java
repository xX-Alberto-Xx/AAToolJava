package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.data.objectives.Objective;
import org.mcsr.aatool.data.speedrunning.StrongholdRing;

public class AllPortals extends Category {
  public static final List<StrongholdRing> RINGS;

  public static final List<String> SUPPORTED_VERSIONS;

  private static final List<Point> TEST_VALUES;

  private int filledPortals;

  public AllPortals() {}

  @Override
  public Iterable<String> getSupportedVersions() {}
  @Override
  public Iterable<Objective> getOverlayObjectives() {}
  @Override
  public int getTargetCount() {}
  @Override
  public int getCompletedCount() {}

  @Override
  public void loadObjectives() {}

  public final void fillPortal(Point coordinates) {}

  public final void clearProgress() {}

  private void updateTotals() {}

  private StrongholdRing closestRing(Point coordinates) {}

  private static double distanceBetween(Point start, Point end) {}
}
