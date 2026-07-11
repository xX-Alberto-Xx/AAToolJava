package org.mcsr.aatool.utilities.easings.functions;

public class Cubic implements EasingFunction {
  @Override
  public final float in(float i) { return i * i * i; }

  @Override
  public final float out(float i) {
    i--;
    return 1 + i * i * i;
  }
}
