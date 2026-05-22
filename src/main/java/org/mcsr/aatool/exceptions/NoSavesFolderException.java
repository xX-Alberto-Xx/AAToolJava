package org.mcsr.aatool.exceptions;

import java.io.IOException;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.enums.TrackerSource;

public class NoSavesFolderException extends IOException {
  public final String missingPath;

  public NoSavesFolderException(String missingPath) {
    super(buildMessage());
    this.missingPath = missingPath;
  }

  private static String buildMessage() {
    if (Config.getTracking().source.getValue() == TrackerSource.ACTIVE_INSTANCE) {
      // TODO: ActiveInstance
    }

    return "Custom saves path doesn't exist";
  }
}
