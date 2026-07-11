package org.mcsr.aatool.utilities.easings;

import java.util.Map;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.enums.Ease;
import org.mcsr.aatool.utilities.Timer;
import org.mcsr.aatool.utilities.easings.functions.*;

public class Easing extends Timer {
  public static final Map<Ease, EasingFunction> FUNCTIONS = Map.of(
    Ease.BACK, new Back(),
    Ease.BOUNCE, new Bounce(),
    Ease.CIRCULAR, new Circular(),
    Ease.CUBIC, new Cubic(),
    Ease.ELASTIC, new Elastic(),
    Ease.EXPONENTIAL, new Exponential(),
    Ease.QUADRATIC, new Quadratic(),
    Ease.QUARTIC, new Quartic(),
    Ease.QUINTIC, new Quintic(),
    Ease.SINUSOIDAL, new Sinusoidal()
  );

  public EasingFunction function;
  public boolean repeats;

  private float scaledTime;

  public Easing(Ease function, double duration) { this(function, duration, false, false); }
  public Easing(Ease function, double duration, boolean startNow) { this(function, duration, startNow, false); }
  public Easing(Ease function, double duration, boolean startNow, boolean repeats) {
    super(duration, startNow);
    this.function = FUNCTIONS.get(function);
    this.repeats = repeats;
  }

  public final float in() { return this.function.in(this.scaledTime); }
  public final float out() { return this.function.out(this.scaledTime); }
  public final float inOut() { return this.function.inOut(this.scaledTime); }

  public final void play(Ease function) {
    this.function = FUNCTIONS.get(function);
    this.reset();
  }

  @Override
  public void update(Time time) {
    super.update(time);
    if (this.isExpired() && this.repeats) this.reset();
    this.scaledTime = (float) Math.min(this.timeElapsed / this.duration, 1);
  }
}
