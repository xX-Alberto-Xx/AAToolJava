package org.mcsr.aatool.utilities;

public class Stopwatch {
  private boolean running;
  private long start;
  private long now;

  public void start() {
    this.running = true;
    this.start = System.nanoTime();
  }

  public long getElapsedMilliseconds() {
    long time = System.nanoTime();
    if (this.running) this.now = time;
    return (this.now - this.start) / 1_000_000;
  }

  public void stop() {
    long time = System.nanoTime();
    this.now = time;
    this.running = false;
  }
}
