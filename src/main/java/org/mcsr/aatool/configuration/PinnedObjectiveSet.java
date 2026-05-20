package org.mcsr.aatool.configuration;

import java.util.List;
import java.util.Map;

public class PinnedObjectiveSet {
  public static final List<String> ALL_AA;

  public static final List<String> ALL_AB;

  public static final List<String> ALL_AACH;

  public Map<String, List<String>> pinned;

  public static List<String> getAllAvailable() {}

  public final boolean tryGetCurrentList(/*out */List<String> list) {}

  public final boolean tryGetCurrentList(String category, String version, /*out */List<String> list) {}

  public final boolean trySetCurrentList(List<UIPinnedObjectiveFrame> frames) {}

  private String getKey(String category, String version) {}
}
