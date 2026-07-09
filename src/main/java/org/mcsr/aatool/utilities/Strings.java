package org.mcsr.aatool.utilities;

import java.util.Locale;
import java.util.regex.Pattern;

public final class Strings {
  private static final Pattern WORD_REGEX = Pattern.compile("(\\w)(\\w*)");

  private Strings() {}

  public static boolean isNullOrEmpty(String str) { return str == null || str.isEmpty(); }
  public static boolean isNullOrBlank(String str) { return str == null || str.isBlank(); }

  public static String toTitleCase(String str) {
    return WORD_REGEX.matcher(str).replaceAll(match ->
      match.group(1).toUpperCase(Locale.US) +
      match.group(2).toLowerCase(Locale.US)
    );
  }
}
