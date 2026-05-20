package org.mcsr.aatool.configuration;

public interface SettingInterface<T> {
  public void set(T value);
  public void applyDefault();
  public void clearFlag();
}
