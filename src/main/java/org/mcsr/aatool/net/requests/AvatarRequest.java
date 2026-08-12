package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.Debug;
import org.mcsr.aatool.Paths;
import org.mcsr.aatool.data.speedrunning.Leaderboard;
import org.mcsr.aatool.net.Player;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.HttpUtils;
import org.mcsr.aatool.utilities.Result;

public final class AvatarRequest extends NetRequest {
  private static int downloads;

  private final Uuid id;
  private final String name;
  private final boolean isFallback;

  public AvatarRequest(Uuid player) { this(player, false); }
  public AvatarRequest(Uuid player, boolean isFallback) {
    super(isFallback ? Paths.Web.getAvatarUrlFallback(player, 8) : Paths.Web.getAvatarUrl(player, 8));
    this.id = player;
    this.isFallback = isFallback;
    String playerName = Player.tryGetName(player).value;
    this.name = playerName != null ? playerName.toLowerCase() : null;
  }

  public AvatarRequest(String name) {
    super(Paths.Web.getAvatarUrl(Leaderboard.getRealName(name).toLowerCase(), 8));
    this.name = Leaderboard.getRealName(name).toLowerCase();
    Uuid playerId = Player.tryGetUuid(this.name);
    this.id = playerId != null ? playerId : Uuid.EMPTY;
    this.isFallback = false;
  }

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    // Logging
    Result<String> nameResult = Player.tryGetName(this.id);
    Debug.log(Debug.REQUEST_SECTION, OUTGOING + " Requested avatar for " + (
      nameResult.success ? '"' + nameResult.value + '"' : this.id.shortString
    ));

    downloads++;
    this.beginTiming();

    // Download texture and add to atlas
    return HttpUtils.getStreamAsync(this.url).thenCompose(response -> {
      try (response) {
        this.endTiming();
        return CompletableFuture.completedStage(this.handleResponse(response));
      } catch (IOException e) {
        return CompletableFuture.failedStage(e);
      }
    }).exceptionallyCompose(e -> {
      if (e instanceof HttpTimeoutException) {
        // Request timed out, nothing left to do here
        Debug.log(Debug.REQUEST_SECTION, "-- Avatar request timed out for " + this.id.shortString);
      } else if (e instanceof IOException) {
        // Error getting response, try other URL
        Debug.log(
          Debug.REQUEST_SECTION,
          "-- Avatar request failed for " + this.id.shortString + ": " + e.getMessage()
        );

        // Try other API
        if (!this.isFallback && !this.id.equals(Uuid.EMPTY)) {
          new AvatarRequest(this.id, true).enqueueOnce();
        }
      } else {
        return CompletableFuture.failedStage(e);
      }

      this.endTiming();
      return CompletableFuture.completedStage(false);
    });
  }

  private boolean handleResponse(InputStream avatarStream) {
    // TODO: Texture2D
  }

  private static void saveToCache(Texture2D texture, Path fileName) {
    try {
      // Cache avatar so it loads instantly next launch
      // Overwrite to keep skins up to date
      Files.createDirectories(Paths.System.AVATAR_CACHE_FOLDER);

      try (OutputStream fileStream = Files.newOutputStream(fileName)) {
        // TODO: Texture2D
      }
    } catch (IOException ignored) {
      // Couldn't save file. Ignore and move on
    }
  }
}
