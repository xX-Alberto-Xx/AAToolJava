package org.mcsr.aatool.utilities;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;

public final class JsonUtils {
  public static final Gson STRICT_GSON = new GsonBuilder().setStrictness(Strictness.STRICT).create();

  private JsonUtils() {}

  public static <T extends JsonElement> T tryParseFile(Path file, Class<T> classOfT) {
    try (Reader reader = Files.newBufferedReader(file)) {
      return STRICT_GSON.fromJson(reader, classOfT);
    } catch (Exception ignored) {
      return null;
    }
  }

  public static String getNullableString(JsonObject obj, String key) {
    JsonElement value = obj.get(key);
    if (value == null) throw new NullPointerException("Missing key: " + key);

    return value instanceof JsonNull ? null : value.getAsJsonPrimitive().getAsString();
  }

  public static String getString(JsonObject obj, String key, String defaultValue) {
    JsonPrimitive primitive = getPrimitive(obj, key);
    return primitive != null ? primitive.getAsString() : defaultValue;
  }

  public static boolean getBoolean(JsonObject obj, String key, boolean defaultValue) {
    JsonPrimitive primitive = getPrimitive(obj, key);
    if (primitive == null) return defaultValue;
    if (primitive.isBoolean()) return primitive.getAsBoolean();

    String value = primitive.getAsString().strip();
    return "true".equalsIgnoreCase(value) ? true
         : "false".equalsIgnoreCase(value) ? false
         : defaultValue;
  }

  public static float getFloat(JsonObject obj, String key, float defaultValue) {
    JsonPrimitive primitive = getPrimitive(obj, key);
    if (primitive == null) return defaultValue;

    try { return primitive.getAsFloat(); }
    catch (NumberFormatException ignored) { return defaultValue; }
  }

  private static JsonPrimitive getPrimitive(JsonObject obj, String key) {
    if (obj == null) return null;

    JsonPrimitive primitive = obj.getAsJsonPrimitive(key);
    return primitive == null || primitive.isString() && primitive.getAsString().isBlank()
           ? null
           : primitive;
  }
}
