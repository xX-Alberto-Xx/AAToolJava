package org.mcsr.aatool.data;

import java.util.List;

import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.Strings;

public class Credit {
  public String name;
  public String highestRole;
  public String currentRole;
  public String link;
  public List<String> altNames;
  public List<Uuid> uuids;

  public Credit() {}

  private Credit(
    String name, String highestRole, String currentRole, String link, List<String> names, List<Uuid> uuids
  ) {
    this.name = name;
    this.highestRole = roleKey(highestRole);
    this.currentRole = roleKey(currentRole);
    this.link = link;
    this.altNames = names;
    this.uuids = uuids;
  }

  public Credit(String highestRole, String currentRole, String name) {
    this(highestRole, currentRole, name, null, "");
  }

  public Credit(String highestRole, String currentRole, String name, Uuid uuid) {
    this(highestRole, currentRole, name, uuid, "");
  }

  public Credit(String highestRole, String currentRole, String name, String link) {
    this(highestRole, currentRole, name, null, link);
  }

  public Credit(String highestRole, String currentRole, String name, Uuid uuid, String link) {
    this(name, highestRole, currentRole, link, List.of(), List.of(uuid != null ? uuid : Uuid.EMPTY));
  }

  public Credit(String highestRole, String currentRole, String name, List<String> names, List<Uuid> uuids) {
    this(name, highestRole, currentRole, "", names, uuids);
  }

  public final boolean isActive() { return !Strings.isNullOrEmpty(this.currentRole); }

  private static String roleKey(String tier) {
    return switch (tier) {
      case "Netherite" -> Credits.NETHERITE_TIER;
      case "Diamond" -> Credits.DIAMOND_TIER;
      case "Gold" -> Credits.GOLD_TIER;
      default -> tier;
    };
  }
}
