package org.mcsr.aatool.data.objectives;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Result;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BlockManifest implements Manifest {
  private Map<String, Block> all = new HashMap<>();
  private Map<String, List<Block>> groups = new HashMap<>();
  private int obtainedCount;
  private int placedCount;

  public List<Block> allBlocksList = new ArrayList<>();

  public BlockManifest() {}

  public final Map<String, Block> getAll() { return this.all; }
  public final Map<String, List<Block>> getGroups() { return this.groups; }
  public final int getObtainedCount() { return this.obtainedCount; }
  public final int getPlacedCount() { return this.placedCount; }
  public final int getCount() { return this.all.size(); }

  public final Result<Block> tryGet(String id) {
    return new Result<>(this.all.containsKey(id), this.all.get(id));
  }

  public final Result<List<Block>> tryGetGroup(String id) {
    return new Result<>(this.groups.containsKey(id), this.groups.get(id));
  }

  @Override
  public final void clearObjectives() {
    this.groups.clear();
    this.all.clear();
    this.placedCount = 0;
    this.obtainedCount = 0;
    this.allBlocksList.clear();
  }

  @Override
  public final void refreshObjectives() {
    this.clearObjectives();

    JsonArray groupsArray = JsonUtils.tryParseFile(
      Paths.System.getObjectiveFolder().resolve("blocks.json"),
      JsonArray.class
    );

    if (groupsArray == null) return;

    // Add block groups from this version
    for (JsonElement groupElem : groupsArray) {
      JsonObject groupObj = groupElem.getAsJsonObject();
      List<Block> group = new ArrayList<>();

      for (JsonElement blockElem : groupObj.getAsJsonArray("blocks")) {
        JsonObject blockObj = blockElem.getAsJsonObject();

        // Add spacer
        if ("empty".equals(blockObj.getAsJsonPrimitive("block_name").getAsString())) {
          group.add(null);
          continue;
        }

        // Add all blocks in group
        Block block = new Block(blockObj);
        this.all.put(block.id, block);
        this.allBlocksList.add(block);
        group.add(block);
      }

      this.groups.put(groupObj.getAsJsonPrimitive("name").getAsString(), group);
    }
  }

  @Override
  public final void updateState(ProgressState progress) {
    for (Block block : this.all.values()) block.updateState(progress);
    this.updateTotal();
  }

  public final void updateTotal() {
    this.placedCount = 0;
    this.obtainedCount = 0;

    for (Block block : this.all.values()) {
      // Update completion count
      if (block.isCompletedByAnyone()) this.placedCount++;
      if (block.isObtained()) this.obtainedCount++;
    }
  }

  private void exportIdList() throws IOException {
    Files.write(
      Path.of("required_blocks_" + Tracker.getCategory().getCurrentVersion()),
      this.all.keySet()
    );
  }
}
