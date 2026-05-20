package org.mcsr.aatool.saves;

import java.util.Map;

import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.net.Uuid;

public abstract class JsonFolder {
  private static final String JSON_PATTERN;

  public final Map<Uuid, JsonStream> players;

  private DirectoryInfo folder;

  public JsonFolder() {}

  public final void setPath(String path) {}

  public final boolean tryRefresh() {}

  public final void update(WorldState state) {}

  protected abstract void update(JsonStream json, WorldState state, Contribution contribution);

  private Map<Uuid, FileInfo> getPlayerJsons() {}

  private boolean tryRemoveDeadFiles(Map<Uuid, FileInfo> newFiles/* = null*/) {}

  private boolean tryAddNewFiles(Map<Uuid, FileInfo> newFiles) {}
}
