package org.mcsr.aatool.utilities;

public class Result<T> {
  public final boolean success;
  public final T value;

  public Result(boolean success, T value) {
    this.success = success;
    this.value = value;
  }
}
