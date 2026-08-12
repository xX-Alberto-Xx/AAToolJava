package org.mcsr.aatool.net.requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.utilities.HttpUtils;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Strings;
import org.mcsr.aatool.utilities.Version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public final class UpdateRequest extends NetRequest {
  private static final String PATCH_NOTES_URL = "https://github.com/xX-Alberto-Xx/AAToolJava/releases/latest/download/patch_notes.json";
  private static final String THUMBNAIL_URL = "https://github.com/xX-Alberto-Xx/AAToolJava/releases/latest/download/thumbnail.png";

  private static List<Pair<String, String>> latestUpgrades = new ArrayList<>();
  private static List<Pair<String, String>> latestFixes = new ArrayList<>();
  private static Version latestVersion;
  private static Texture2D latestThumb;
  private static String latestTitle;

  public static boolean suppress;
  public static boolean userInitiated;

  public UpdateRequest() { this(false); }
  public UpdateRequest(boolean isManual) {
    super(PATCH_NOTES_URL);
    suppress = false;
    userInitiated = isManual;
  }

  public static List<Pair<String, String>> getLatestUpgrades() { return latestUpgrades; }
  public static List<Pair<String, String>> getLatestFixes() { return latestFixes; }
  public static Version getLatestVersion() { return latestVersion; }
  public static Texture2D getLatestThumb() { return latestThumb; }
  public static String getLatestTitle() { return latestTitle; }

  public static boolean isDone() { return latestVersion != null; }

  public static boolean updatesAreAvailable() {
    if (latestVersion == null) return false;

    // TODO: Main
  }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {
    // Get latest update information from GitHub
    return HttpUtils.getStringAsync(PATCH_NOTES_URL).thenCompose(latestJson ->
      HttpUtils.getStreamAsync(THUMBNAIL_URL).thenCompose(imageStream -> {
        try (imageStream) {
          // TODO: Texture2D
        } catch (IOException e) {
          return CompletableFuture.failedStage(e);
        }

        return CompletableFuture.completedStage(handleResponse(latestJson));
      })
    ).exceptionallyCompose(e ->
      // Nothing to do if network error / timed out / image preview is malformed
      e instanceof IOException // TODO: Check for malformed image preview exception
      ? CompletableFuture.completedStage(false)
      : CompletableFuture.failedStage(e)
    );
  }

  private static boolean handleResponse(String latestJson) {
    if (Strings.isNullOrEmpty(latestJson)) return false;

    try {
      JsonObject latestPatch = JsonUtils.STRICT_GSON.fromJson(latestJson, JsonObject.class);

      // Get version
      latestTitle = latestPatch.getAsJsonPrimitive("title").getAsString();
      String vnum = latestPatch.getAsJsonPrimitive("version").getAsString();

      // Populate change lists
      readItems(latestPatch.getAsJsonArray("upgrades"), "bullet_point", latestUpgrades);
      readItems(latestPatch.getAsJsonArray("fixes"), "bullet_fix", latestFixes);

      Version version = Version.tryParse(vnum);

      if (version != null) {
        latestVersion = version;
        return true;
      }
    } catch (JsonSyntaxException | NullPointerException | ClassCastException | IllegalStateException ignored) {
      // Malformed response, nothing to do here
    }

    if (userInitiated) {
      // TODO: UI
    }

    return false;
  }

  private static void readItems(JsonArray items, String defaultIcon, List<Pair<String, String>> dest) {
    dest.clear();

    for (JsonElement itemElem : items) {
      if (itemElem.isJsonPrimitive()) {
        dest.add(new Pair<>(itemElem.getAsString(), defaultIcon));
        continue;
      }

      JsonObject item = itemElem.getAsJsonObject();
      dest.add(new Pair<>(
        item.getAsJsonPrimitive("item").getAsString(),
        item.getAsJsonPrimitive("icon").getAsString()
      ));
    }
  }
}
