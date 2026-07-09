package org.mcsr.aatool.net;

import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Result;
import org.mcsr.aatool.utilities.Strings;

import com.google.gson.JsonObject;

public final class Player {
  private static final Pattern NAME_REGEX = Pattern.compile("^\\w{3,16}$");
  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  private static final HttpRequest.Builder PARTIAL_UUID_REQUEST = HttpRequest.newBuilder()
    .timeout(Duration.ofMillis(Protocol.Requests.TIMEOUT_NORMAL_MS))
    .GET();

  public static final Map<String, Uuid> ID_CACHE = new HashMap<>();
  public static final Map<Uuid, String> NAME_CACHE = new HashMap<>();

  public static final Map<Uuid, Color> ID_COLOR_CACHE = new HashMap<>();
  public static final Map<String, Color> NAME_COLOR_CACHE = new HashMap<>();

  public static final Set<String> NAMES_ALREADY_REQUESTED = new HashSet<>();
  public static final Set<Uuid> IDENTITIES_ALREADY_REQUESTED = new HashSet<>();

  // Invalidation comes from within an async net request
  // We need to ensure it's updated before we call update on the current screen
  // Using two variables allows for us to ensure this through some basic synchronization
  private static boolean identityCacheInvalidatedPrivate;
  public static boolean identityCacheInvalidated;

  private Player() {}

  public static Uuid tryGetUuid(String name) {
    Uuid id = Uuid.tryParse(name);
    return id != null ? id : ID_CACHE.get(name != null ? name : "");
  }

  public static Result<String> tryGetName(Uuid id) {
    return new Result<>(NAME_CACHE.containsKey(id), NAME_CACHE.get(id));
  }

  public static Result<Color> tryGetColor(Uuid id) {
    boolean isCached = ID_COLOR_CACHE.containsKey(id);

    return new Result<>(
      !id.equals(Uuid.EMPTY) && isCached,
      isCached ? ID_COLOR_CACHE.get(id) : new Color(0, 0, 0, 0)
    );
  }

  public static Color tryGetColor(String name) {
    return NAME_COLOR_CACHE.get(name != null ? name : "");
  }

  public static boolean validateName(String name) {
    // Can only contain "A-Z", "a-z", "0-9", and "_"
    return name != null && NAME_REGEX.matcher(name).matches();
  }

  public static CompletableFuture<Uuid> fetchUuidAsync(String name) {
    if (!validateName(name)) return CompletableFuture.completedFuture(Uuid.EMPTY);

    Uuid cached = ID_CACHE.get(name);
    if (cached != null) return CompletableFuture.completedFuture(cached);

    URI uuidUrl;
    try { uuidUrl = new URI(Paths.Web.getUuidUrl(name)); }
    catch (URISyntaxException ignored) { return CompletableFuture.completedFuture(Uuid.EMPTY); }

    return HTTP_CLIENT.sendAsync(
      PARTIAL_UUID_REQUEST.uri(uuidUrl).build(),
      HttpResponse.BodyHandlers.ofString()
    ).thenApply(response -> {
      if (response.statusCode() != 200) return Uuid.EMPTY;

      String body = response.body();
      if (Strings.isNullOrEmpty(body)) return Uuid.EMPTY;

      Uuid id = Uuid.tryParse(
        JsonUtils.STRICT_GSON.fromJson(body, JsonObject.class)
          .getAsJsonPrimitive("id").getAsString()
      );

      if (id == null) return Uuid.EMPTY;

      cache(id, name);
      // TODO: AvatarRequest
      return id;
    }).exceptionally(ignored -> Uuid.EMPTY);
  }

  public static void cache(Uuid id, String name) {
    if (id.equals(Uuid.EMPTY)) return;

    if (!NAME_CACHE.containsKey(id) && !Strings.isNullOrEmpty(name)) {
      NAME_CACHE.put(id, name);
    }

    if (name != null && !id.equals(Uuid.EMPTY)) ID_CACHE.putIfAbsent(name, id);

    if (Objects.equals(name, Config.getTracking().soloFilterName.getValue())) {
      Config.getTracking().soloFilterName.invokeChange();
    }

    identityCacheInvalidatedPrivate = true;
  }

  public static void setFlags() {
    identityCacheInvalidated = identityCacheInvalidatedPrivate;
  }

  public static void clearFlags() {
    if (identityCacheInvalidated) {
      identityCacheInvalidated = identityCacheInvalidatedPrivate = false;
    }
  }

  public static void cache(Uuid id, Color color) { ID_COLOR_CACHE.put(id, color); }
  public static void cache(String name, Color color) { NAME_COLOR_CACHE.put(name, color); }

  public static void fetchIdentityAsync(Uuid id) {
    if (!IDENTITIES_ALREADY_REQUESTED.add(id)) return;

    // TODO: NameRequest
    // TODO: AvatarRequest
  }

  public static void fetchIdentityAsync(String name) {
    if (!NAMES_ALREADY_REQUESTED.add(name)) return;

    // TODO: UuidRequest
  }
}
