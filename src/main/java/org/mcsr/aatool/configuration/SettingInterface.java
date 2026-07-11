package org.mcsr.aatool.configuration;

public interface SettingInterface<T> {
  void set(T value);
  void applyDefault();
  void clearFlag();
}
