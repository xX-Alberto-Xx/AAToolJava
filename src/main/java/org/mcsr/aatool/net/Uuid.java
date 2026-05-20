package org.mcsr.aatool.net;

import java.util.UUID;

public class Uuid {
  public static final Uuid EMPTY;

  public final String string;
  public final String shortString;

  private final UUID innerID;

  public Uuid(String stringForm) {}

  private Uuid(UUID id) {}

  public static boolean tryParse(String stringForm, /*out */Uuid uuid) {}

  @Override
  public boolean equals(Object obj) {}
  @Override
  public int hashCode() {}
  @Override
  public String toString() {}
}
