package org.mcsr.aatool.utilities.easings.functions;

public class Bounce implements EasingFunction {
  private static final float FIRST_BOUNCE = 1 / 2.75f;
  private static final float SECOND_BOUNCE = 2 / 2.75f;
  private static final float THIRD_BOUNCE = 2.5f / 2.75f;
  private static final float A = (1 / FIRST_BOUNCE) * (1 / FIRST_BOUNCE);
  private static final float HALF_FIRST_GAP = (SECOND_BOUNCE - FIRST_BOUNCE) / 2;
  private static final float FIRST_OFFSET = 1 - A * HALF_FIRST_GAP * HALF_FIRST_GAP;
  private static final float HALF_SECOND_GAP = (THIRD_BOUNCE - SECOND_BOUNCE) / 2;
  private static final float SECOND_OFFSET = 1 - A * HALF_SECOND_GAP * HALF_SECOND_GAP;
  private static final float HALF_THIRD_GAP = (1 - THIRD_BOUNCE) / 2;
  private static final float THIRD_OFFSET = 1 - A * HALF_THIRD_GAP * HALF_THIRD_GAP;

  @Override
  public final float in(float i) { return 1 - this.out(1 - i); }

  @Override
  public final float out(float i) {
    return i < FIRST_BOUNCE ? parabola(i)
         : i < SECOND_BOUNCE ? parabola(i - (FIRST_BOUNCE + SECOND_BOUNCE) / 2) + FIRST_OFFSET
         : i < THIRD_BOUNCE ? parabola(i - (SECOND_BOUNCE + THIRD_BOUNCE) / 2) + SECOND_OFFSET
         : parabola(i - (THIRD_BOUNCE + 1) / 2) + THIRD_OFFSET;
  }

  private static float parabola(float i) { return A * i * i; }
}
