package org.mcsr.aatool.utilities.easings.functions;

public class Elastic implements EasingFunction {
  private static final double TWO_TO_THE_TENTH = 1024;
  private static final double TWO_TO_THE_MINUS_TENTH = 0.0009765625;

  @Override
  public final float in(float i) {
    return i == 0 || i == 1 ? i : -elastic(TWO_TO_THE_TENTH, i - 1);
  }

  @Override
  public final float out(float i) {
    return i == 0 || i == 1 ? i : elastic(TWO_TO_THE_MINUS_TENTH, i) + 1;
  }

  @Override
  public final float inOut(float i) {
    return i < 0.5f
           ? elastic(TWO_TO_THE_TENTH, i * 2 - 1) * -0.5f
           : elastic(TWO_TO_THE_MINUS_TENTH, i * 2 - 1) * 0.5f + 1f;
  }

  private static float elastic(double base, float i) {
    return (float) (Math.pow(base, i) * Math.sin((i - 0.1) * (2 * Math.PI / 0.4)));
  }
}
