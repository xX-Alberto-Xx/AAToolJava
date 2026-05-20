package org.mcsr.aatool.configuration;

public class Setting<T> implements SettingInterface<T> {
  private T value;

  private boolean changed;

  public final T defaultValue;

  public Setting(T defaultValue) {}

  public final T getValue() { return this.value; }

  public final boolean isChanged() { return this.changed; }

  @Override
  public final void set(T newValue) {}

  @Override
  public final void applyDefault() {}

  @Override
  public final void clearFlag() {}

  public final void invokeChange() {}
}
