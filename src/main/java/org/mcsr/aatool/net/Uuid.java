package org.mcsr.aatool.net;

import java.util.Objects;
import java.util.UUID;

public class Uuid {
  private static final UUID JAVA_EMPTY = new UUID(0, 0);
  public static final Uuid EMPTY = new Uuid(JAVA_EMPTY);

  public final String string;
  public final String shortString;

  private final UUID innerID;

  public Uuid(String stringForm) {
    this.string = Objects.requireNonNullElse(stringForm, "");
    this.shortString = this.string.replace("-", "");
    this.innerID = parseJava(this.string);
  }

  private Uuid(UUID id) {
    this.innerID = id;
    this.string = id.toString();
    this.shortString = this.string.replace("-", "");
  }

  private static UUID parseJava(String stringForm) {
    try { return UUID.fromString(stringForm); }
    catch (IllegalArgumentException ignored) { return JAVA_EMPTY; }
  }

  public static Uuid parse(String stringForm) {
    try { return new Uuid(UUID.fromString(stringForm)); }
    catch (IllegalArgumentException ignored) { return EMPTY; }
  }

  @Override
  public boolean equals(Object obj) { return obj instanceof Uuid uuid && this.string.equals(uuid.string); }
  @Override
  public int hashCode() { return this.string.hashCode(); }
  @Override
  public String toString() { return this.string; }
}
