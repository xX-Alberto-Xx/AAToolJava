package org.mcsr.aatool.net;

import java.util.Map;
import java.util.Set;

public final class Player {
  public static final Map<String, Uuid> ID_CACHE;
  public static final Map<Uuid, String> NAME_CACHE;

  public static final Map<Uuid, Color> ID_COLOR_CACHE;
  public static final Map<String, Color> NAME_COLOR_CACHE;

  public static final Set<String> NAMES_ALREADY_REQUESTED;
  public static final Set<Uuid> IDENTITIES_ALREADY_REQUESTED;

  private static boolean identityCacheInvalidatedPrivate;
  public static boolean identityCacheInvalidated;

  private Player() {}

  public static boolean tryGetUuid(String name, /*out */Uuid id) {}
  public static boolean tryGetName(Uuid id, /*out */String name) {}
  public static boolean tryGetColor(Uuid id, /*out */Color color) {}
  public static boolean tryGetColor(String name, /*out */Color color) {}

  public static boolean validateName(String name) {}

  public static Task<Uuid> fetchUuidAsync(String name) {}

  public static void cache(Uuid id, String name) {}

  public static void setFlags() {}

  public static void clearFlags() {}

  public static void cache(Uuid id, Color color) {}

  public static void cache(String name, Color color) {}

  public static void fetchIdentityAsync(Uuid id) {}

  public static void fetchIdentityAsync(String name) {}
}
