package org.mcsr.aatool.utilities;

public final class Strings {
  private Strings() {}

  public static boolean isNullOrEmpty(String str) { return str == null || str.isEmpty(); }
  public static boolean isNullOrBlank(String str) { return str == null || str.isBlank(); }
}
