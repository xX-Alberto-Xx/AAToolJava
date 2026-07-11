package org.mcsr.aatool.net;

import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.utilities.JsonUtils;

public final class Lobby {
  public final Map<Uuid, User> users = new HashMap<>();
  public final Map<String, Uuid> designations = new HashMap<>();
  private Uuid hostId = Uuid.EMPTY;

  public int getUserCount() { return this.users.size(); }

  public User tryGetHost() { return this.users.get(this.hostId); }

  public void setHost(User user) {
    this.users.put(user.id, user);
    this.hostId = user.id;
  }

  public static Lobby fromJsonString(String jsonString) {
    Lobby lobby = JsonUtils.STRICT_GSON.fromJson(jsonString, Lobby.class);

    // Attempt to load player identities
    for (Uuid id : lobby.users.keySet()) Player.fetchIdentityAsync(id);
    return lobby;
  }

  public String toJsonString() { return JsonUtils.STRICT_GSON.toJson(this); }

  public User tryGetUser(Uuid id) { return this.users.get(id); }

  public void add(User user) { this.users.put(user.id, user); }

  public void remove(User user) { this.users.remove(user.id); }
}
