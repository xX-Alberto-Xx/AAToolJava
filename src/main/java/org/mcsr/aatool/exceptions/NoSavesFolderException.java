package org.mcsr.aatool.exceptions;

import java.io.IOException;

public class NoSavesFolderException extends IOException {
  public final String missingPath;

  public NoSavesFolderException(String missingPath) {}

  private static String buildMessage() {}
}
