package org.mcsr.aatool.utilities.easings.functions;

public class Quadratic implements EasingFunction {
  @Override
  public final float in(float i) { return i * i; }

  @Override
  public final float out(float i) { return i * (2 - i); }

  public final float bezier(float i, float c) { return c * 2 * i * (1 - i) + i * i; }
}
