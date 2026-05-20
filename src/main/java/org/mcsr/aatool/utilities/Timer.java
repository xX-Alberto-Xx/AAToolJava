package org.mcsr.aatool.utilities;

import org.mcsr.aatool.Time;

public class Timer {
  public double duration;
  public double timeLeft;
  public double timeElapsed;

  public Timer() {}
  public Timer(double duration, boolean startNow/* = false*/) {}

  public final boolean isExpired() {}
  public final boolean isRunning() {}

  public final double getNormalized() {}

  public final void setAndStart(double duration) {}

  public final void setAndStop(double duration) {}

  public final void reset() {}

  public final void expire() {}

  public void update(Time time) {}
}
