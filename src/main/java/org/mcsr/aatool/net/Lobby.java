package org.mcsr.aatool.net;

import java.util.Map;

public final class Lobby {
  public final Map<Uuid, User> users;
  public final Map<String, Uuid> designations;
  private Uuid hostId;

  public Lobby() {}

  public final int getUserCount() {}

  public final boolean tryGetHost(/*out */User host) {}

  public final void setHost(User user) {}

  public static Lobby fromJsonString(String jsonString) {}

  public final String toJsonString() {}

  public final boolean tryGetUser(Uuid id, /*out */User user) {}

  public final void add(User user) {}

  public final void remove(User user) {}
}
