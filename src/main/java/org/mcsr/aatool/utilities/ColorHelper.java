package org.mcsr.aatool.utilities;

import java.awt.Color;
import java.util.HexFormat;

public final class ColorHelper {
  private static final HexFormat HEX_STYLE = HexFormat.of().withUpperCase();

  private ColorHelper() {}

  public static Color fromRGB(int r, int g, int b) { return new Color(r, g, b); }
  public static Color fromRGBA(int r, int g, int b, int a) { return new Color(r, g, b, a); }

  public static Color fade(Color color, float opacity) {
    return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
  }

  public static Color fromHSV(double hue, double sat, double val) {
    hue /= 60;
    val *= 255;

    double f = hue % 1;
    int v = (int) Math.rint(val);
    int p = (int) Math.rint(val * (1 - sat));
    int q = (int) Math.rint(val * (1 - f * sat));
    int t = (int) Math.rint(val * (1 - (1 - f) * sat));

    return switch ((int) Math.floor(hue) % 6) {
      case 0 -> new Color(v, t, p);
      case 1 -> new Color(q, v, p);
      case 2 -> new Color(p, v, t);
      case 3 -> new Color(p, q, v);
      case 4 -> new Color(t, p, v);
      default -> new Color(v, p, q);
    };
  }

  public static String toHexString(Color color) {
    return "#" + HEX_STYLE.toHexDigits(color.getRGB(), 6);
  }

  public static Color tryGetHexColor(String hex) {
    try {
      // Remove # if present
      if (hex.charAt(0) == '#') hex = hex.substring(1);

      // Support both long and short hex codes
      if (hex.length() == 6) {
        byte[] rgb = HEX_STYLE.parseHex(hex);
        return fromRGB(rgb[0], rgb[1], rgb[2]);
      }

      if (hex.length() == 3) {
        int rgb = 0;

        for (int i = 0; i < 3; i++) {
          int digit = Character.digit(hex.charAt(i), 16);
          if (digit == -1) return null;

          rgb = rgb << 8 | digit << 4 | digit;
        }

        return new Color(rgb);
      }

      return fromRGB(0, 0, 0);
    } catch (Exception e) {
      return null;
    }
  }

  public static Color blend(Color fore, Color back, double amount) {
    return new Color(
      (int) (fore.getRed() * amount + back.getRed() * (1 - amount)),
      (int) (fore.getGreen() * amount + back.getGreen() * (1 - amount)),
      (int) (fore.getBlue() * amount + back.getBlue() * (1 - amount))
    );
  }

  public static Color amplify(Color color, float amount) {
    float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
    return fromHSV(hsb[0], hsb[1] * amount, hsb[2]);
  }

  public static Color getAccent(Texture2D texture) {
    // TODO: Texture2D
  }
}
