package org.mcsr.aatool.net;

public final class NetworkHelper {
  private NetworkHelper() {}

  public static boolean tryGetLocalIPAddress(/*out */IPAddress ip) {}

  public static byte[] compressString(String text) {}

  public static boolean tryDecompressString(byte[] compressed, /*out */String text) {}

  public static byte[] compressBytes(byte[] decompressed) {}

  public static boolean tryDecompressBytes(byte[] compressed, /*out */byte[] decompressed) {}
}
