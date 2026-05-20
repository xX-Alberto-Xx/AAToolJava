package org.mcsr.aatool.data;

import java.util.List;

import org.mcsr.aatool.net.Uuid;

public class Credit {
  public String name;
  public String highestRole;
  public String currentRole;
  public String link;
  public List<String> altNames;
  public List<Uuid> uuids;

  public Credit(String highestRole, String currentRole, String name, Uuid/*?*/ uuid/* = null*/, String link/* = ""*/) {}

  public Credit(String highestRole, String currentRole, String name, String link) {}

  public Credit(String highestRole, String currentRole, String name, List<String> names, List<Uuid> uuids) {}

  public final boolean isActive() {}

  private static String roleKey(String tier) {}
}
