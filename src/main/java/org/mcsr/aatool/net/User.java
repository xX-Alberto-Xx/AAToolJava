package org.mcsr.aatool.net;

public class User {
  private static final int MAX_NAME_LENGTH;
  private static final String ELLIPSIS;

  public static final User NOBODY;

  public final Uuid id;
  public final String pronouns;
  private final String preferredName;

  public User(Uuid id, String pronouns, String preferredName/* = null*/) {}

  @Override
  public int hashCode() {}

  @Override
  public boolean equals(Object obj) {}

  public final String getName() {}
}
