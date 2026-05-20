package org.mcsr.aatool;

public final class Time {
  private static final int SAMPLE_SIZE;

  private double totalSeconds;
  private long totalFrames;
  private double delta;
  private double averageFPS;
  private double currentFPS;

  private static final Queue<Double> SAMPLE_BUFFER;

  public double getTotalSeconds() { return this.totalSeconds; }
  public long getTotalFrames() { return this.totalFrames; }
  public double getDelta() { return this.delta; }
  public double getAverageFPS() { return this.averageFPS; }
  public double getCurrentFPS() { return this.currentFPS; }

  public void update(GameTime gameTime) {}
}
