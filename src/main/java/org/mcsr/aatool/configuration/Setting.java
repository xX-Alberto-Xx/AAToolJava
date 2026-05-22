package org.mcsr.aatool.configuration;

public class Setting<T> implements SettingInterface<T> {
  private T value;

  private boolean changed;

  public final T defaultValue;

  public Setting(T defaultValue) {
    this.defaultValue = defaultValue;
    this.applyDefault();
  }

  public final T getValue() { return this.value; }

  public final boolean isChanged() { return this.changed; }

  @Override
  public final void set(T newValue) {
    if (newValue != null && !newValue.equals(this.value)) {
      this.value = newValue;
      this.changed = true;
    }
  }

  @Override
  public final void applyDefault() { this.set(this.defaultValue); }

  @Override
  public final void clearFlag() { this.changed = false; }

  public final void invokeChange() { this.changed = true; }
}
