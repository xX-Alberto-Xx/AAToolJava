package org.mcsr.aatool.utilities;

public final class SearchUtils {
  private SearchUtils() {}

  public static <T> boolean contains(T[] arr, T value) {
    for (T elem : arr) {
      if (elem.equals(value)) return true;
    }

    return false;
  }

  public static <T> boolean contains(Iterable<T> iterable, T value) {
    for (T item : iterable) {
      if (item.equals(value)) return true;
    }

    return false;
  }
}
