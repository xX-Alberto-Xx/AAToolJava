package org.mcsr.aatool.utilities.easings;

import java.util.Map;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.enums.Ease;
import org.mcsr.aatool.utilities.Timer;
import org.mcsr.aatool.utilities.easings.functions.EasingFunction;

public class Easing extends Timer {
  public static final Map<Ease, EasingFunction> FUNCTIONS;

  public EasingFunction function;
  public boolean repeats;

  private float scaledTime;

  public Easing(Ease function, double duration, boolean startNow/* = false*/, boolean repeats/* = false*/) {}

  public final float in() {}
  public final float out() {}
  public final float inOut() {}

  public final void play(Ease function) {}

  @Override
  public void update(Time time) {}
}
