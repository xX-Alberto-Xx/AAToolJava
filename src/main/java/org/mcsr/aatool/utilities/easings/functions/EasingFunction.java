package org.mcsr.aatool.utilities.easings.functions;

public interface EasingFunction {
  float in(float i);
  float out(float i);

  default float inOut(float i) {
    return i < 0.5f
           ? this.in(i * 2) * 0.5f
           : this.out(i * 2 - 1) * 0.5f + 0.5f;
  }
}
