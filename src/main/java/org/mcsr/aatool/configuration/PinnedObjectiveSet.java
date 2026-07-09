package org.mcsr.aatool.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.utilities.Result;
import org.mcsr.aatool.utilities.Version;

public class PinnedObjectiveSet {
  public static final List<String> ALL_AA = List.of(
    "EGap", "Trident", "NautilusShells", "WitherSkulls",
    "AncientDebris", "GoldBlocks", "Bees", "Sniffers",
    "Cats", "Foods", "Animals", "Monsters", "Biomes", "Cauldrons", "ArmorTrims", "HeavyCore"
  );

  public static final List<String> ALL_AB = List.of(
    "Trident", "NautilusShells", "ShulkerShells", "WitherSkulls",
    "AncientDebris", "DeepslateEmerald", "SculkBlocks", "Mycelium", "RedSand", "Bees", "HeavyCore"
  );

  public static final List<String> ALL_AACH = List.of(
    "EGap", "WitherSkulls", "GoldBlocks", "Biomes"
  );

  public Map<String, List<String>> pinned = Map.ofEntries(
    Map.entry("All Advancements 1.21.6", List.of(
      "WitherSkulls", "NautilusShells", "Trident", "HeavyCore", "Sniffers", "ArmorTrims"
    )),
    Map.entry("All Advancements 1.21 v2", List.of(
      "WitherSkulls", "NautilusShells", "Trident", "HeavyCore", "Sniffers", "ArmorTrims"
    )),
    Map.entry("All Advancements 1.21", List.of(
      "WitherSkulls", "NautilusShells", "Trident", "Sniffers", "ArmorTrims"
    )),
    Map.entry("All Advancements 1.20.5", List.of(
      "WitherSkulls", "NautilusShells", "Trident", "Sniffers", "ArmorTrims"
    )),
    Map.entry("All Advancements 1.20 v2", List.of(
      "WitherSkulls", "NautilusShells", "Trident", "Sniffers", "ArmorTrims"
    )),
    Map.entry("All Advancements 1.20", List.of(
      "AncientDebris", "WitherSkulls", "NautilusShells", "Trident", "EGap", "ArmorTrims"
    )),
    Map.entry("All Advancements 1.19", List.of(
      "AncientDebris", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.18", List.of(
      "AncientDebris", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.17", List.of(
      "Cauldrons", "AncientDebris", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.16.5", List.of(
      "AncientDebris", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.16", List.of(
      "AncientDebris", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.15", List.of(
      "GoldBlocks", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.14", List.of(
      "Cats", "GoldBlocks", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.13", List.of(
      "GoldBlocks", "WitherSkulls", "NautilusShells", "Trident", "EGap"
    )),
    Map.entry("All Advancements 1.12", List.of(
      "GoldBlocks", "WitherSkulls", "Monsters", "Biomes", "EGap"
    )),
    Map.entry("All Achievements 1.11", List.of(
      "GoldBlocks", "WitherSkulls", "Biomes", "EGap"
    )),
    Map.entry("All Blocks 1.21", List.of(
      "DeepslateEmerald", "HeavyCore", "WitherSkulls", "ShulkerShells", "NautilusShells", "Trident"
    )),
    Map.entry("All Blocks 1.20", List.of(
      "AncientDebris", "DeepslateEmerald", "WitherSkulls", "ShulkerShells", "NautilusShells", "Trident"
    )),
    Map.entry("All Blocks 1.19", List.of(
      "AncientDebris", "DeepslateEmerald", "WitherSkulls", "ShulkerShells", "NautilusShells", "Trident"
    )),
    Map.entry("All Blocks 1.18", List.of(
      "AncientDebris", "DeepslateEmerald", "WitherSkulls", "ShulkerShells", "NautilusShells", "Trident"
    )),
    Map.entry("All Blocks 1.16", List.of(
      "AncientDebris", "Mycelium", "WitherSkulls", "ShulkerShells", "NautilusShells", "Trident"
    ))
  );

  public static List<String> getAllAvailable() {
    List<String> available = new ArrayList<>(
      Tracker.getCategory() instanceof AllBlocks ? ALL_AB :
      Tracker.getCategory() instanceof AllAchievements ? ALL_AACH :
      ALL_AA
    );

    Version current = Version.tryParse(Tracker.getCurrentVersion());

    if (current == null) {
      // Latest snapshot
      available.remove("Cauldrons");
      return available;
    }

    if (current.isBefore(1, 21)) available.remove("HeavyCore");
    if (current.isBefore(1, 20)) available.removeAll(Set.of("ArmorTrims", "Sniffers"));
    if (current.isBefore(1, 19)) available.remove("SculkBlocks");
    if (current.isNot(1, 17)) available.remove("Cauldrons");
    if (current.isBefore(1, 17)) available.remove("DeepslateEmerald");
    if (current.isBefore(1, 16)) available.remove("AncientDebris");
    if (current.isBefore(1, 15)) available.remove("Bees");
    if (current.isBefore(1, 14)) available.remove("Cats");
    if (current.isBefore(1, 13)) available.removeAll(Set.of("Trident", "NautilusShells"));

    return available;
  }

  public final Result<List<String>> tryGetCurrentList() {
    return this.tryGetCurrentList(Tracker.getCategory().getName(), Tracker.getCategory().getCurrentVersion());
  }

  public final Result<List<String>> tryGetCurrentList(String category, String version) {
    String key = this.getKey(category, version);
    return new Result<>(this.pinned.containsKey(key), this.pinned.get(key));
  }

  public final boolean trySetCurrentList(List<UIPinnedObjectiveFrame> frames) {
    // TODO: UIPinnedObjectiveFrame
  }

  private String getKey(String category, String version) {
    String lastKey = category + ' ' + version;

    // Get the most recent revision of the default list if present
    String base = lastKey + " v";
    int revision = 2;

    while (true) {
      String key = base + revision;
      if (!this.pinned.containsKey(key)) return lastKey;

      lastKey = key;
      revision++;
    }
  }
}
