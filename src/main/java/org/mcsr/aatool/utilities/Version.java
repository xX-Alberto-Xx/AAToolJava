package org.mcsr.aatool.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Version {
  private static final Pattern PATTERN = Pattern.compile("([0-9]+)\\.([0-9]+)(?:\\.([0-9]+)(?:\\.([0-9]+)))");

  public final int major;
  public final int minor;
  public final int build;
  public final int revision;

  public Version(int major, int minor, int build, int revision) {
    this.major = major;
    this.minor = minor;
    this.build = build;
    this.revision = revision;
  }

  public boolean isBefore(Version other) {
    return this.major < other.major || this.major == other.major && (
      this.minor < other.minor || this.minor == other.minor && (
        this.build < other.build || this.build == other.build && (
          this.revision < other.revision
        )
      )
    );
  }

  public boolean isBefore(int major, int minor) {
    return this.major < major || this.major == major && this.minor < minor;
  }

  public boolean isNot(int major, int minor) {
    return this.major != major || this.minor != minor || this.build != 0 || this.revision != 0;
  }

  public static Version tryParse(String input) {
    if (input == null) return null;

    Matcher matcher = PATTERN.matcher(input);
    if (!matcher.matches()) return null;

    try {
      int major = Integer.parseInt(matcher.group(1));
      int minor = Integer.parseInt(matcher.group(2));

      String buildDigits = matcher.group(3);
      if (buildDigits == null) return new Version(major, minor, -1, -1);

      int build = Integer.parseInt(buildDigits);
      String revisionDigits = matcher.group(4);
      if (revisionDigits == null) return new Version(major, minor, build, -1);

      return new Version(major, minor, build, Integer.parseInt(revisionDigits));
    } catch (NumberFormatException ignored) {
      return null;
    }
  }
}
