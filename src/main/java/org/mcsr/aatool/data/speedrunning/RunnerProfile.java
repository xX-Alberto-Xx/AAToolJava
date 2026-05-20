package org.mcsr.aatool.data.speedrunning;

import java.util.Map;

public class RunnerProfile {
  public static Map<String, RunnerProfile> profilesByIdOrName;
  public static Map<String, String> namesBySrcId;
  public static Map<String, String> srcIdsByName;

  public String id;
  public String name;
  public String pronouns;
  public String link;
  public Texture2D picture;

  public static RunnerProfile getCurrent() {}

  public static void initialize() {}

  public static void setCurrentId(String id) {}

  public static void setCurrentName(String name) {}

  public static boolean tryParseSrc(String json, boolean cache, /*out */RunnerProfile profile) {}

  private static void cache(String json, String idOrName) {}

  public static boolean tryReadCached(String idOrName, /*out */RunnerProfile profile) {}
}
