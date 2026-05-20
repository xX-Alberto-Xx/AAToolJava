package org.mcsr.aatool.data.progress;

import org.mcsr.aatool.net.Uuid;

public class Completion {
  public static final Completion EMPTY;

  public Uuid player;
  public DateTime timestamp;

  public Completion(Uuid player, DateTime timestamp) {}

  public final boolean isEmpty() {}

  public final boolean before(DateTime timestamp) {}
  public final boolean after(DateTime timestamp) {}
}
