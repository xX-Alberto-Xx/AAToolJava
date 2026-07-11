package org.mcsr.aatool.utilities.easings.functions;

public class Exponential implements EasingFunction {
  private static final double TWO_TO_THE_TENTH = 1024;
  private static final double TWO_TO_THE_MINUS_TENTH = 0.0009765625;

  @Override
  public final float in(float i) {
    return i == 0 ? 0 : (float) Math.pow(TWO_TO_THE_TENTH, i - 1);
  }

  @Override
  public final float out(float i) {
    return i == 1 ? 1 : 1 - (float) Math.pow(TWO_TO_THE_MINUS_TENTH, i);
  }
}
