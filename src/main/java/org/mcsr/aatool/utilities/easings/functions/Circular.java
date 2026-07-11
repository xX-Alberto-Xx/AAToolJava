package org.mcsr.aatool.utilities.easings.functions;

public class Circular implements EasingFunction {
  @Override
  public final float in(float i) { return 1 - (float) Math.sqrt(1 - i * i); }

  @Override
  public final float out(float i) {
    i--;
    return (float) Math.sqrt(1 - i * i);
  }
}
