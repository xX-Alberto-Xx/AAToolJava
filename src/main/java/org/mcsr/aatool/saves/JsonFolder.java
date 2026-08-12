package org.mcsr.aatool.saves;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.progress.Contribution;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.net.Player;
import org.mcsr.aatool.net.Uuid;

public abstract class JsonFolder {
  private static final String JSON_PATTERN = "*.json";

  public final Map<Uuid, JsonStream> players = new HashMap<>();

  private Path folder;

  public final void setPath(Path path) {
    if (Paths.isNullOrEmpty(path)) {
      this.folder = null;
      return;
    }

    if (this.folder == null || !path.equals(this.folder.toAbsolutePath())) {
      this.players.clear();
      this.folder = path;
    }
  }

  public final boolean tryRefresh() {
    // Get all UUID JSON files in folder
    Map<Uuid, Path> foundPlayers = this.getPlayerJsons();
    boolean modified = this.tryRemoveDeadFiles(foundPlayers) | this.tryAddNewFiles(foundPlayers);

    // Update JSONs
    for (JsonStream json : this.players.values()) {
      modified |= json.tryRefresh(Tracker.areObjectivesChanged());
    }

    return modified;
  }

  public final void update(WorldState state) {
    for (Map.Entry<Uuid, JsonStream> player : this.players.entrySet()) {
      Uuid key = player.getKey();
      Contribution contribution;

      if (state.players.containsKey(key)) {
        contribution = state.players.get(key);
      } else {
        contribution = new Contribution(key);
        state.players.put(key, contribution);
      }

      this.update(player.getValue(), state, contribution);
    }
  }

  protected abstract void update(JsonStream json, WorldState state, Contribution contribution);

  private Map<Uuid, Path> getPlayerJsons() {
    Map<Uuid, Path> foundPlayers = new HashMap<>();

    // Get all JSON files in folder
    Stream<Path> files = Paths.tryGetAllFiles(this.folder, JSON_PATTERN, false);
    if (files == null) return foundPlayers;

    try (files) {
      files.forEach(file -> {
        Uuid id = Uuid.tryParse(Paths.getFileNameWithoutExtension(file));

        if (id != null) {
          // Filename is UUID
          foundPlayers.put(id, file);
          Player.fetchIdentityAsync(id);
        }
      });
    } catch (UncheckedIOException ignored) {}

    return foundPlayers;
  }

  private boolean tryRemoveDeadFiles() { return this.tryRemoveDeadFiles(null); }
  private boolean tryRemoveDeadFiles(Map<Uuid, Path> newFiles) {
    boolean modified = false;

    for (Iterator<Uuid> it = this.players.keySet().iterator(); it.hasNext();) {
      Uuid id = it.next();

      if (newFiles == null || !newFiles.containsKey(id)) {
        it.remove();
        modified = true;
      }
    }

    return modified;
  }

  private boolean tryAddNewFiles(Map<Uuid, Path> newFiles) {
    boolean modified = false;

    for (Map.Entry<Uuid, Path> file : newFiles.entrySet()) {
      Uuid key = file.getKey();
      if (this.players.containsKey(key)) continue;

      this.players.put(key, new JsonStream(file.getValue().toAbsolutePath(), key));
      modified = true;
    }

    return modified;
  }
}
