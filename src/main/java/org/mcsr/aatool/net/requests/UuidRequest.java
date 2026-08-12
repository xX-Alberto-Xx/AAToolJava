package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Debug;
import org.mcsr.aatool.Paths;
import org.mcsr.aatool.net.Player;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.HttpUtils;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Strings;

import com.google.gson.JsonObject;

public final class UuidRequest extends NetRequest {
  private static int downloads;

  private final String name;
  private final boolean requestAvatar;

  public UuidRequest(String name) { this(name, false); }
  public UuidRequest(String name, boolean requestAvatar) {
    super(Paths.Web.getUuidUrl(name));
    this.name = name;
    this.requestAvatar = requestAvatar;
  }

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    // Logging
    Debug.log(Debug.REQUEST_SECTION, OUTGOING + " Requested UUID for \"" + this.name + '"');
    downloads++;
    this.beginTiming();

    // Get Minecraft UUID and add to cache
    return HttpUtils.getStringAsync(this.url).thenApply(response -> {
      this.endTiming();
      return this.handleResponse(response);
    }).exceptionallyCompose(e -> {
      if (e instanceof HttpTimeoutException) {
        // Request timed out, nothing left to do here
        Debug.log(Debug.REQUEST_SECTION, "-- UUID request timed out for \"" + this.name + '"');
      } else if (e instanceof IOException) {
        // Error getting response, safely move on
        Debug.log(Debug.REQUEST_SECTION, "-- UUID request failed for \"" + this.name + "\": " + e.getMessage());
      } else {
        return CompletableFuture.failedStage(e);
      }

      this.endTiming();
      return CompletableFuture.completedStage(false);
    });
  }

  private boolean handleResponse(String response) {
    if (Strings.isNullOrEmpty(response)) return false;

    Uuid id = Uuid.tryParse(
      JsonUtils.STRICT_GSON.fromJson(response, JsonObject.class)
        .getAsJsonPrimitive("id").getAsString()
    );

    if (id == null) {
      Debug.log(
        Debug.REQUEST_SECTION,
        INCOMING + " Received invalid UUID for \"" + this.name + "\" (" + response + ") in " + this.getResponseTime()
      );
      return false;
    }

    Debug.log(
      Debug.REQUEST_SECTION,
      INCOMING + " Received UUID for \"" + this.name + "\" (" + response + ") in " + this.getResponseTime()
    );

    Player.cache(id, this.name);
    if (this.requestAvatar) new AvatarRequest(id).enqueueOnce();
    return true;
  }
}
