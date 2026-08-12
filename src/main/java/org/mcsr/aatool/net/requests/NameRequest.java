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

import com.google.gson.JsonObject;

public final class NameRequest extends NetRequest {
  private static int downloads;

  private final Uuid id;
  private final String shortId;

  public NameRequest(Uuid id) {
    super(Paths.Web.getNameUrl(id.toString().replace("-", "")));
    this.id = id;
    this.shortId = id.toString().replace("-", "");
  }

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    Debug.log(Debug.REQUEST_SECTION, "Name requested for UUID: " + this.shortId);
    downloads++;
    this.beginTiming();

    // Get Minecraft name and add to cache
    return HttpUtils.getStringAsync(this.url).thenApply(response -> {
      this.endTiming();
      return this.handleResponse(response);
    }).exceptionallyCompose(e -> {
      if (e instanceof HttpTimeoutException) {
        Debug.log(Debug.REQUEST_SECTION, "-- Name request timed out for UUID: " + this.shortId);
        // Request timed out, nothing left to do here
      } else if (e instanceof IOException) {
        Debug.log(Debug.REQUEST_SECTION, "-- Name request failed for UUID: " + this.shortId + ": " + e.getMessage());
        // Error getting response, safely move on
      } else {
        return CompletableFuture.failedStage(e);
      }

      this.endTiming();
      return CompletableFuture.completedStage(false);
    });
  }

  private boolean handleResponse(String response) {
    response = response.strip();
    if (response.isEmpty()) return false;

    Player.cache(
      this.id,
      JsonUtils.STRICT_GSON.fromJson(response, JsonObject.class)
        .getAsJsonPrimitive("name").getAsString()
    );

    Debug.log(
      Debug.REQUEST_SECTION,
      INCOMING + " Received name \"" + response + "\" for UUID: " + this.shortId + " in " + this.getResponseTime()
    );

    return true;
  }
}
