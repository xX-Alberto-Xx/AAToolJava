package org.mcsr.aatool.saves;

import org.mcsr.aatool.net.Uuid;

public class JsonStream {
  public final Uuid player;
  public final String fullName;

  private DateTime lastWriteTime;

  private dynamic jsonData;
  private boolean isAlive;

  public JsonStream(String fullName, Uuid player) {}

  public final DateTime getLastWriteTime() { return this.lastWriteTime; }

  public final dynamic get(String key) {}

  @Override
  public String toString() {}

  private void close(StreamReader reader) {}

  public final boolean tryRefresh(boolean ignoreTimestamps) {}

  private boolean tryRead(StreamReader stream) {}

  private boolean needsRefresh() {}

  private boolean tryGetLastWriteTime(/*out */DateTime lastWriteTime) {}

  private boolean tryOpen(String path, /*out */StreamReader reader) {}
}
