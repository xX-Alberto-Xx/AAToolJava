package org.mcsr.aatool.utilities.easings.functions;

public class Back implements EasingFunction {
  private static final float S1 = 1.70158f;
  private static final float S2 = 2.5949095f;

  @Override
  public final float in(float i) { return i * i * ((S1 + 1) * i - S1); }

  @Override
  public final float out(float i) {
    i--;
    return i * i * ((S1 + 1) * i + S1) + 1;
  }

  @Override
  public final float inOut(float i) {
    i *= 2;
    if (i < 1) return 0.5f * i * i * ((S2 + 1) * i - S2);

    i -= 2;
    return 0.5f * i * i * ((S2 + 1) * i + S2) + 1;
  }
}
