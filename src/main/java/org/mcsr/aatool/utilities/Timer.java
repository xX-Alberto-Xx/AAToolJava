package org.mcsr.aatool.utilities;

import org.mcsr.aatool.Time;

public class Timer {
  public double duration;
  public double timeLeft;
  public double timeElapsed;

  public Timer() {}

  public Timer(double duration) { this(duration, false); }
  public Timer(double duration, boolean startNow) {
    this.duration = duration;
    if (startNow) this.timeLeft = duration;
  }

  public final boolean isExpired() { return this.timeLeft <= 0; }
  public final boolean isRunning() { return this.timeLeft > 0; }

  public final double getNormalized() {
    return this.duration > 0 ? Math.max(this.timeLeft, 0) / this.duration : 0;
  }

  public final void setAndStart(double duration) {
    this.duration = duration;
    this.reset();
  }

  public final void setAndStop(double duration) {
    this.duration = duration;
    this.expire();
  }

  public final void reset() {
    this.timeLeft = this.duration;
    this.timeElapsed = 0;
  }

  public final void expire() { this.timeLeft = 0; }

  public void update(Time time) {
    double delta = time.getDelta();
    this.timeElapsed += delta;
    this.timeLeft -= delta;
  }
}
