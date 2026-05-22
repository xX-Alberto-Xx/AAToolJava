package org.mcsr.aatool.utilities;

import java.util.Locale;

public enum OperatingSystem {
  WINDOWS, MAC_OS, LINUX;

  public static final OperatingSystem CURRENT;

  static {
    String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    CURRENT = osName.contains("win") ? WINDOWS : osName.contains("mac") ? MAC_OS : LINUX;
  }
}
