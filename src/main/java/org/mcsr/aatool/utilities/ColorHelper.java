package org.mcsr.aatool.utilities;

public final class ColorHelper {
  private static final NumberStyles HEX_STYLE;

  private ColorHelper() {}

  public static Color fromRGB(int r, int g, int b) {}
  public static Color fromRGBA(int r, int g, int b, int a) {}

  public static Color fade(Color color, float opacity) {}
  public static Color fromHSV(double hue, double sat, double val) {}

  public static String toHexString(Color color) {}

  public static boolean tryGetHexColor(String hex, /*out */Color color) {}

  public static Color blend(Color fore, Color back, double amount) {}

  public static Color amplify(Color color, float amount) {}

  public static Color getAccent(Texture2D texture) {}
}
