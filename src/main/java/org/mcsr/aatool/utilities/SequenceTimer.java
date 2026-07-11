package org.mcsr.aatool.utilities;

public class SequenceTimer extends Timer {
  private int index = -1;

  private final double[] sequence;

  public SequenceTimer(double... sequence) {
    this.sequence = sequence.clone();
    this.resume();
  }

  public final int getIndex() { return this.index; }

  public final double getRatio() { return this.timeLeft / this.sequence[this.index]; }

  public final void resume() { this.setAndStart(this.nextDuration(1)); }

  public final void startFromBeginning() {
    this.index = 0;
    this.setAndStart(this.sequence[0]);
  }

  public final void skip(int count) { this.setAndStart(this.nextDuration(count)); }

  private double nextDuration(int count) {
    this.index = (this.index + count) % this.sequence.length;
    return this.sequence[this.index];
  }
}
