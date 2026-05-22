package org.mcsr.aatool.exceptions;

import java.io.IOException;
import java.nio.file.Path;

import org.mcsr.aatool.configuration.Config;

public class NoWorldException extends IOException {
  public NoWorldException() { super(buildMessage()); }

  private static String buildMessage() {
    return switch (Config.getTracking().source.getValue()) {
      case ACTIVE_INSTANCE -> {} // TODO: ActiveInstance
      case SPECIFIC_WORLD -> {
        try {
          Path name = Config.getTracking().customWorldPath.getValue().getFileName();
          yield "Specified world \"" + name + "\" doesn't exist";
        } catch (Exception ignored) {
          yield "Specified world invalid";
        }
      }
      case CUSTOM_SAVES_PATH -> "No worlds in custom save path";
    };
  }
}
