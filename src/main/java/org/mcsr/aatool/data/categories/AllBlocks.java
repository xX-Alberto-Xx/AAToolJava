package org.mcsr.aatool.data.categories;

import java.util.List;

import org.mcsr.aatool.data.objectives.Objective;

public class AllBlocks extends Category {
  public static final String MAIN_TEXTURE_SET;
  public static final String HELP_TEXTURE_SET;

  public static final List<String> SUPPORTED_VERSIONS;

  public static boolean mainSpritesLoaded;
  public static boolean helpSpritesLoaded;

  private static boolean writingChecklistFile;

  public int blocksHighlightedCount;
  public int blocksConfirmedCount;

  public AllBlocks() {}

  @Override
  public Iterable<String> getSupportedVersions() {}
  @Override
  public Iterable<Objective> getOverlayObjectives() {}

  @Override
  public int getTargetCount() {}
  @Override
  public int getCompletedCount() {}

  @Override
  public String getStatus() {}

  @Override
  public void loadObjectives() {}

  public final void clearHighlighted() {}

  public final void clearConfirmed() {}

  @Override
  public void update() {}

  public final String getBlockHighlights() {}

  public final int countHighlightedBlocks() {}

  public final int countConfirmedBlocks() {}

  public final void applyChecklist(String[] lines) {}

  public final void saveChecklist() {}

  private void tryWriteChecklist(String list) {}

  private void tryLoadChecklist() {}

  private void exportBlockList() {}
}
