package org.mcsr.aatool.data.objectives;

import java.util.List;
import java.util.Map;

import org.mcsr.aatool.data.progress.ProgressState;

public class BlockManifest implements Manifest {
  private Map<String, Block> all;
  private Map<String, List<Block>> groups;
  private int obtainedCount;
  private int placedCount;

  public List<Block> allBlocksList;

  public BlockManifest() {}

  public final Map<String, Block> getAll() { return this.all; }
  public final Map<String, List<Block>> getGroups() { return this.groups; }
  public final int getObtainedCount() { return this.obtainedCount; }
  public final int getPlacedCount() { return this.placedCount; }
  public final int getCount() {}

  public final boolean tryGet(String id, /*out */Block block) {}

  public final boolean tryGetGroup(String id, /*out */List<Block> group) {}

  public final void clearObjectives() {}

  public final void refreshObjectives() {}

  public final void updateState(ProgressState progress) {}

  public final void updateTotal() {}

  private void exportIdList() {}
}
