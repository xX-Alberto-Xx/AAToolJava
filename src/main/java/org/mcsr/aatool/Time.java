package org.mcsr.aatool;

import java.util.ArrayDeque;
import java.util.Queue;

public final class Time {
  private static final int SAMPLE_SIZE = 60;

  private double totalSeconds;
  private long totalFrames;
  private double delta;
  private double averageFPS;
  private double currentFPS;

  private static final Queue<Double> SAMPLE_BUFFER = new ArrayDeque<>(SAMPLE_SIZE + 1);

  public double getTotalSeconds() { return this.totalSeconds; }
  public long getTotalFrames() { return this.totalFrames; }
  public double getDelta() { return this.delta; }
  public double getAverageFPS() { return this.averageFPS; }
  public double getCurrentFPS() { return this.currentFPS; }

  public void update(double delta) {
    // Time since last frame
    this.delta = delta;
    this.currentFPS = 1 / delta;

    // Calculate FPS
    SAMPLE_BUFFER.add(this.currentFPS);

    if (SAMPLE_BUFFER.size() > SAMPLE_SIZE) {
      SAMPLE_BUFFER.remove();
      double sum = 0;
      for (double i : SAMPLE_BUFFER) sum += i;
      this.averageFPS = sum / SAMPLE_SIZE;
    } else {
      this.averageFPS = this.currentFPS;
    }

    this.totalFrames++;
    this.totalSeconds += delta;
  }
}
