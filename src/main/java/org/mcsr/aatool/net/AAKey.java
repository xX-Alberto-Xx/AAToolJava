package org.mcsr.aatool.net;

public class AAKey {
  public static final String RANDOM_CHARACTERS = "0123456789";
  public static final String PREFIX = "AAKEY-";
  public static final int LENGTH = 16;

  private AAKey() {}

  public static String strip(String fullKey) {
    return fullKey != null ? fullKey.replace(PREFIX, "").replace("-", "") : null;
  }
}
