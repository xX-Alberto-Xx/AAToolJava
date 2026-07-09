package org.mcsr.aatool.data.categories;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.Block;
import org.mcsr.aatool.data.objectives.Objective;

public class AllBlocks extends Category {
  public static final String MAIN_TEXTURE_SET = "blocks";
  public static final String HELP_TEXTURE_SET = "ab_guide";

  public static final List<String> SUPPORTED_VERSIONS = List.of(
    "1.21", "1.20", "1.19", "1.18", "1.16"
  );

  public static boolean mainSpritesLoaded;
  public static boolean helpSpritesLoaded;

  private static boolean writingChecklistFile = false;

  public int blocksHighlightedCount;
  public int blocksConfirmedCount;

  public AllBlocks() {
    this.name = "All Blocks";
    this.acronym = "AB";
    this.objective = "Blocks";
    this.action = "Placed";

    // TODO: SpriteSheet
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS; }
  @Override
  public Iterable<? extends Objective> getOverlayObjectives() { return Tracker.BLOCKS.getAll().values(); }

  @Override
  public int getTargetCount() { return Tracker.BLOCKS.getCount(); }
  @Override
  public int getCompletedCount() { return Tracker.BLOCKS.getPlacedCount(); }

  @Override
  public String getStatus() {
    int obtainedCount = Tracker.BLOCKS.getObtainedCount();

    return super.getStatus() + " (" + (
      obtainedCount > 0 ? "Approximately " + obtainedCount : obtainedCount
    ) + " Obtained)";
  }

  @Override
  public void loadObjectives() {
    Tracker.BLOCKS.refreshObjectives();
    Tracker.COMPLEX_OBJECTIVES.refreshObjectives();
  }

  public final void clearHighlighted() {
    for (Block block : Tracker.BLOCKS.getAll().values()) {
      if (!block.isComplete()) block.highlighted = false;
    }
  }

  public final void clearConfirmed() {
    for (Block block : Tracker.BLOCKS.getAll().values()) {
      if (block.isComplete()) block.highlighted = false;
    }
  }

  @Override
  public void update() {
    if (Tracker.isSavesFolderChanged() || Tracker.isWorldChanged()) {
      this.clearHighlighted();
      this.clearConfirmed();
      this.tryLoadChecklist();
    }
  }

  public final String getBlockHighlights() {
    StringBuilder builder = new StringBuilder();

    for (Block block : Tracker.BLOCKS.getAll().values()) {
      if (block.highlighted) builder.append(block.getId()).append('\n');
    }

    return builder.toString();
  }

  public final int countHighlightedBlocks() {
    int counter = 0;

    for (Block block : Tracker.BLOCKS.getAll().values()) {
      if (!block.isComplete() && block.highlighted) counter++;
    }

    return counter;
  }

  public final int countConfirmedBlocks() {
    int counter = 0;

    for (Block block : Tracker.BLOCKS.getAll().values()) {
      if (block.isComplete() && block.highlighted) counter++;
    }

    return counter;
  }

  public final void applyChecklist(String[] lines) {
    if (lines == null) return;

    for (String id : lines) {
      // TODO: Tracker
    }
  }

  public final void saveChecklist() {
    if (!Tracker.isWorking()) return; // TODO: Peer

    this.blocksHighlightedCount = 0;
    this.blocksConfirmedCount = 0;
    StringBuilder list = new StringBuilder();

    for (Block block : Tracker.BLOCKS.getAll().values()) {
      if (!block.highlighted) continue;

      list.append(block.getId()).append('\n');
      // Update counts
      if (block.isComplete()) this.blocksConfirmedCount++;
      else this.blocksHighlightedCount++;
    }

    this.tryWriteChecklist(list.toString());
  }

  private void tryWriteChecklist(String list) {
    if (writingChecklistFile) return;
    writingChecklistFile = true;

    new Thread(() -> {
      try {
        Files.createDirectories(Paths.System.BLOCK_CHECKLISTS_FOLDER);
        // TODO: ActiveInstance
      } catch (IOException ignored) {
      } finally {
        writingChecklistFile = false;
      }
    }).start();
  }

  private void tryLoadChecklist() {
    this.blocksHighlightedCount = 0;
    this.blocksConfirmedCount = 0;
    if (!Tracker.isWorking()) return; // TODO: Peer

    try {
      // TODO: ActiveInstance
    } catch (Exception ignored) {
    }
  }

  private void exportBlockList() throws IOException {
    StringBuilder list = new StringBuilder();

    for (Block block : Tracker.BLOCKS.getAll().values()) {
      list.append(block.getName().replace('\n', ' ')).append('\n');
    }

    Files.writeString(
      Path.of("all_required_blocks_" + Tracker.getCategory().getCurrentMajorVersion() + ".txt"),
      list.toString()
    );
  }
}
