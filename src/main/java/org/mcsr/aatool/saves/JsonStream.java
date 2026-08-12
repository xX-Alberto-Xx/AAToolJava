package org.mcsr.aatool.saves;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.JsonUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonStream implements Iterable<Map.Entry<String, JsonElement>> {
  public final Uuid player;
  public final Path fullName;

  private Instant lastWriteTime = Instant.MIN;

  private JsonObject jsonData;
  private boolean isAlive;

  public JsonStream(Path fullName, Uuid player) {
    this.fullName = fullName;
    this.player = player;
  }

  public final Instant getLastWriteTime() { return this.lastWriteTime; }

  public final JsonElement get(String key) { return this.jsonData != null ? this.jsonData.get(key) : null; }

  @Override
  public Iterator<Map.Entry<String, JsonElement>> iterator() {
    return this.jsonData != null ? this.jsonData.entrySet().iterator() : Collections.emptyIterator();
  }

  @Override
  public String toString() { return this.jsonData != null ? this.jsonData.toString() : ""; }

  public final boolean tryRefresh(boolean ignoreTimestamps) {
    // Handle file timestamps and attempt to read
    if (!(ignoreTimestamps || this.needsRefresh())) return false;

    InputStream stream = this.tryOpen(this.fullName);
    if (stream != null && this.tryRead(stream)) this.isAlive = true;
    else if (this.isAlive) this.isAlive = false;
    else return false;

    return true;
  }

  private boolean tryRead(InputStream stream) {
    try (stream) {
      // Read all JSON file contents and deserialize into JsonObject
      this.jsonData = JsonUtils.STRICT_GSON.fromJson(
        new String(stream.readAllBytes(), StandardCharsets.UTF_8),
        JsonObject.class
      );

      return true;
    } catch (Exception ignored) {
      this.jsonData = null;
      return false;
    }
  }

  private boolean needsRefresh() {
    Instant latestWriteTime = this.tryGetLastWriteTime();

    if (latestWriteTime == null) {
      this.isAlive = false;
      return false;
    }

    if (!latestWriteTime.equals(this.lastWriteTime)) return false;

    this.lastWriteTime = latestWriteTime;
    return true;
  }

  private Instant tryGetLastWriteTime() {
    try { return Files.getLastModifiedTime(this.fullName).toInstant(); }
    catch (IOException ignored) { return null; }
  }

  private InputStream tryOpen(Path path) {
    try {
      // On Windows, NIO sets the FILE_SHARE_READ, FILE_SHARE_WRITE and FILE_SHARE_DELETE flags for free
      return Files.newInputStream(path);
    } catch (IOException e) {
      if (!(e instanceof NoSuchFileException)) this.isAlive = false;
      return null;
    }
  }
}
