package org.mcsr.aatool.utilities.easings.functions;

public class Quartic implements EasingFunction {
  @Override
  public final float in(float i) {
    i *= i;
    return i * i;
  }

  @Override
  public final float out(float i) {
    i--;
    i *= i;
    return 1 - i * i;
  }
}
