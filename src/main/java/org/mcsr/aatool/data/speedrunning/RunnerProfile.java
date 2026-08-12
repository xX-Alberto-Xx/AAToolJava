package org.mcsr.aatool.data.speedrunning;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.configuration.TrackingConfig;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Strings;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class RunnerProfile {
  public static Map<String, RunnerProfile> profilesByIdOrName = new HashMap<>();
  public static Map<String, String> namesBySrcId = new HashMap<>();
  public static Map<String, String> srcIdsByName = new HashMap<>();

  public String id;
  public String name;
  public String pronouns;
  public String link;
  public Texture2D picture;

  public static RunnerProfile getCurrent() {
    return profilesByIdOrName.get(Config.getTracking().getCurrentRunnerProfileNameOrId().toLowerCase());
  }

  public static void initialize() {
    TrackingConfig trackingConfig = Config.getTracking();
    String currentRunnerProfileId = trackingConfig.currentRunnerProfileId.getValue();

    if (!Strings.isNullOrEmpty(currentRunnerProfileId)) {
      setCurrentId(currentRunnerProfileId);
      return;
    }

    String currentRunnerProfileName = trackingConfig.currentRunnerProfileName.getValue();

    if (!Strings.isNullOrEmpty(currentRunnerProfileName)) {
      setCurrentName(currentRunnerProfileName);
    }
  }

  public static void setCurrentId(String id) {
    TrackingConfig trackingConfig = Config.getTracking();
    trackingConfig.currentRunnerProfileId.set(id);
    trackingConfig.currentRunnerProfileName.set("");
    trackingConfig.trySave();

    if (getCurrent() != null) return;

    RunnerProfile cached = tryReadCached(id);

    if (cached != null) {
      profilesByIdOrName.put(cached.name.toLowerCase(), cached);
      profilesByIdOrName.put(cached.id, cached);
      return;
    }

    RunnerProfile newProfile = new RunnerProfile();
    newProfile.id = id;

    if (!Strings.isNullOrEmpty(id)) {
      profilesByIdOrName.put(id, newProfile);
      newProfile.name = namesBySrcId.get(id);
    }
  }

  public static void setCurrentName(String name) {
    TrackingConfig trackingConfig = Config.getTracking();
    trackingConfig.currentRunnerProfileName.set(name);
    trackingConfig.currentRunnerProfileId.set("");
    trackingConfig.trySave();

    if (getCurrent() != null) return;

    RunnerProfile cached = tryReadCached(name);

    if (cached != null) {
      profilesByIdOrName.put(cached.name.toLowerCase(), cached);
      profilesByIdOrName.put(cached.id, cached);
      return;
    }

    RunnerProfile newProfile = new RunnerProfile();
    newProfile.name = name;

    if (!Strings.isNullOrEmpty(name)) {
      profilesByIdOrName.put(name.toLowerCase(), newProfile);
      newProfile.id = srcIdsByName.get(name);
    }
  }

  public static RunnerProfile tryParseSrc(String json, boolean cache) {
    try {
      JsonObject data = JsonUtils.STRICT_GSON.fromJson(json, JsonObject.class).getAsJsonObject("data");
      String id = data.getAsJsonPrimitive("id").getAsString();
      String name = data.getAsJsonObject("names").getAsJsonPrimitive("international").getAsString();

      RunnerProfile profile = new RunnerProfile();
      profile.id = id;
      profile.name = name;
      profile.pronouns = JsonUtils.getNullableString(data, "pronouns");
      profile.link = data.getAsJsonPrimitive("weblink").getAsString();
      profilesByIdOrName.put(name.toLowerCase(), profile);
      profilesByIdOrName.put(id, profile);

      if (cache) {
        cache(json, id);
        cache(json, name);
      }

      return profile;
    } catch (
      JsonSyntaxException | NullPointerException |
      ClassCastException | IllegalStateException ignored
    ) {
      return null;
    }
  }

  private static void cache(String json, String idOrName) {
    try {
      Files.createDirectories(Paths.System.PROFILE_DETAILS_CACHE_FOLDER);
      Files.writeString(Paths.System.speedrunDotComProfileJson(idOrName), json);
    } catch (IOException ignored) {}
  }

  public static RunnerProfile tryReadCached(String idOrName) {
    if (Strings.isNullOrEmpty(idOrName)) return null;

    try {
      String json;
      try { json = Files.readString(Paths.System.speedrunDotComProfileJson(idOrName)); }
      catch (NoSuchFileException ignored) { return null; }

      RunnerProfile profile = tryParseSrc(json, false);
      if (profile == null) return null;

      try (Reader reader = Files.newBufferedReader(Paths.System.speedrunDotComProfilePicture(profile.id))) {
        // TODO: Texture2D
      } catch (NoSuchFileException ignored) {}

      return profile;
    } catch (IOException ignored) {
      return null;
    }
  }
}
