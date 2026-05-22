package org.mcsr.aatool.utilities;

public final class ArrayUtils {
  private ArrayUtils() {}

  public static <T> boolean contains(T[] arr, T val) {
    for (T elem : arr) {
      if (elem == val || val.equals(elem)) return true;
    }

    return false;
  }
}
