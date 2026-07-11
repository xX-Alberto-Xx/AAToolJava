package org.mcsr.aatool.utilities.easings.functions;

public class Sinusoidal implements EasingFunction {
  @Override
  public final float in(float i) { return 1 - (float) Math.cos(i * (Math.PI / 2)); }

  @Override
  public final float out(float i) { return (float) Math.sin(i * (Math.PI / 2)); }

  @Override
  public final float inOut(float i) { return 0.5f - 0.5f * (float) Math.cos(i * Math.PI); }
}
