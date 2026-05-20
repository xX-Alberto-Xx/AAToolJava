package org.mcsr.aatool.data;

import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.net.Uuid;

public final class Credits {
  public static final String DEVELOPER;
  public static final String DEDICATION;
  public static final String BETA_TESTER;

  public static final String GOLD_TIER;
  public static final String DIAMOND_TIER;
  public static final String NETHERITE_TIER;

  public static final String CTM;
  public static final String CTM_NAME;

  public static final String ELYSAKU;
  public static final String ELYSAKU_NAME;

  public static final String COURIWAY;
  public static final String COURIWAY_NAME;

  public static final String MOLEY_G;
  public static final String MOLEY_G_NAME;

  public static final String DEADPOOL;
  public static final String DEADPOOL_NAME;

  public static final String CAPTAIN_SPARKLEZ;
  public static final String CAPTAIN_SPARKLEZ_NAME;

  public static final String ILLUMINA;
  public static final String ILLUMINA_NAME;

  public static final String FEINBERG;
  public static final String FEINBERG_NAME;

  public static final Set<Credit> SPECIAL;

  private static boolean initialized;

  private static Set<Credit> all;

  private static Map<String, Credit> byName;
  private static Map<Uuid, Credit> byUuid;

  private Credits() {}

  public static Set<Credit> getAll() { return all; }

  private static void ensureLookupsInitialized() {}

  public static boolean tryGet(Uuid player, /*out */Credit credit) {}

  public static boolean tryGet(String name, /*out */Credit credit) {}

  public static void initialize() {}

  public static boolean syncSheet(String csv) {}

  private static boolean tryLoadCached() {}
}
