package org.mcsr.aatool;

import java.util.Map;

public final class Debug {
  public static final String GRAPHICS_SECTION;
  public static final String SYSTEM_SECTION;
  public static final String TRACKING_SECTION;
  public static final String REQUEST_SECTION;
  public static final String ERROR_SECTION;

  public static final Map<String, Stopwatch> WATCHES;
  public static final Map<String, StringBuilder> LOGS;
  public static final StringBuilder GLOBAL_LOG;

  public static boolean enableTiming;

  private Debug() {}

  public static String getGlobalLog() {}

  public static String getLog(String section) {}

  public static void beginTiming(String name) {}

  public static void endTiming(String name) {}

  public static void log(String section, String message) {}

  public static void saveReport(Exception exception) {}
}
