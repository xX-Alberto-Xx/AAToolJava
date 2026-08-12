package org.mcsr.aatool.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

public final class NetworkHelper {
  private NetworkHelper() {}

  public static InetAddress tryGetLocalIPAddress() {
    try { return InetAddress.getLocalHost(); }
    catch (UnknownHostException ignored) { return null; }
  }

  public static byte[] compressString(String text) {
    return compressBytes(text != null ? text.getBytes(StandardCharsets.US_ASCII) : new byte[] {});
  }

  public static String tryDecompressString(byte[] compressed) {
    byte[] decompressed = tryDecompressBytes(compressed);
    return decompressed != null ? new String(compressed, StandardCharsets.US_ASCII) : null;
  }

  public static byte[] compressBytes(byte[] decompressed) {
    // Compress byte array using deflate algorithm
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    try (DeflaterOutputStream deflate = new DeflaterOutputStream(
      output, new Deflater(Deflater.DEFAULT_COMPRESSION, true)
    )) {
      deflate.write(decompressed);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return output.toByteArray();
  }

  public static byte[] tryDecompressBytes(byte[] compressed) {
    // Decompress byte array using deflate algorithm
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    try (InflaterOutputStream deflate = new InflaterOutputStream(output, new Inflater(true))) {
      deflate.write(compressed);
      return output.toByteArray();
    } catch (IOException ignored) {
      return null;
    }
  }
}
