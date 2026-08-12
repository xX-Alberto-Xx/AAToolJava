package org.mcsr.aatool.data.progress;

import java.time.Instant;

import org.mcsr.aatool.net.Uuid;

public class Completion {
  public static final Completion EMPTY = new Completion(Uuid.EMPTY, Instant.MIN);

  public final Uuid player;
  public final Instant timestamp;

  public Completion(Uuid player, Instant timestamp) {
    this.player = player;
    this.timestamp = timestamp;
  }

  public final boolean isEmpty() { return this.player.equals(Uuid.EMPTY); }

  public final boolean before(Instant timestamp) { return this.timestamp.isBefore(timestamp); }
  public final boolean after(Instant timestamp) { return this.timestamp.isAfter(timestamp); }
}
