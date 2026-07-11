package org.mcsr.aatool.utilities.easings.functions;

public class Quintic implements EasingFunction {
  @Override
  public final float in(float i) {
    float ii = i * i;
    return ii * ii * i;
  }

  @Override
  public final float out(float i) {
    i--;
    float ii = i * i;
    return 1 + ii * ii * i;
  }
}
