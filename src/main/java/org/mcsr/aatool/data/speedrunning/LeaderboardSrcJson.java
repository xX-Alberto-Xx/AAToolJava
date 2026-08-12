package org.mcsr.aatool.data.speedrunning;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.mcsr.aatool.utilities.JsonUtils;
import org.mcsr.aatool.utilities.Version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class LeaderboardSrcJson {
  public List<Run> runs;

  private LeaderboardSrcJson(List<Run> runs) { this.runs = runs; }

  public static LeaderboardSrcJson tryParse(String json, String version) {
    try {
      JsonObject data = JsonUtils.STRICT_GSON.fromJson(json, JsonObject.class).getAsJsonObject("data");
      JsonArray players = data.getAsJsonObject("players").getAsJsonArray("data");

      for (JsonElement playerElement : players) {
        JsonObject player = playerElement.getAsJsonObject();

        JsonPrimitive idPrimitive = player.getAsJsonPrimitive("id");
        if (idPrimitive == null || !idPrimitive.isString()) continue;

        JsonObject names = player.getAsJsonObject("names");
        if (names == null) continue;

        JsonPrimitive international = names.getAsJsonPrimitive("international");
        if (international == null || !international.isString()) continue;

        String id = idPrimitive.getAsString();
        String name = international.getAsString();
        RunnerProfile.namesBySrcId.put(id, name);
        RunnerProfile.srcIdsByName.put(name, id);
      }

      JsonArray runs = data.getAsJsonArray("runs");
      List<Run> runList = new ArrayList<>();

      for (JsonElement runRoot : runs) {
        try {
          Run parsedRun = new Run();
          parsedRun.gameVersion = Version.tryParse(version);

          JsonObject run = runRoot.getAsJsonObject().getAsJsonObject("run");
          JsonObject times = run.getAsJsonObject("times");
          JsonPrimitive ingameT = times.getAsJsonPrimitive("ingame_t");
          JsonPrimitive realtimeT = times.getAsJsonPrimitive("realtime_t");

          parsedRun.inGameTime = Duration.ofMillis(Math.round((
            ingameT.isNumber()
            ? ingameT.getAsDouble()
            : parseTimeString(times.getAsJsonPrimitive("ingame").getAsString())
          ) * 1000));

          parsedRun.realTime = Duration.ofMillis(Math.round((
            realtimeT.isNumber()
            ? realtimeT.getAsDouble()
            : parseTimeString(times.getAsJsonPrimitive("realtime").getAsString())
          ) * 1000));

          try {
            parsedRun.date = LocalDate.parse(run.getAsJsonPrimitive("date").getAsString());
          } catch (DateTimeParseException ignored) {
            parsedRun.date = LocalDate.MIN;
          }

          String id = run.getAsJsonArray("players").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsString();
          parsedRun.runner = RunnerProfile.namesBySrcId.getOrDefault(id, "<error>");
          parsedRun.runnerSrcId = id;
          parsedRun.verifiable = true;
          parsedRun.status = run.getAsJsonObject("status").getAsJsonPrimitive("status").getAsString();
          parsedRun.comment = run.getAsJsonPrimitive("comment").getAsString();
          parsedRun.setLink(run.getAsJsonPrimitive("weblink").getAsString());
          runList.add(parsedRun);
        } catch (Exception ignored) {}
      }

      return new LeaderboardSrcJson(runList);
    } catch (
      JsonSyntaxException | NullPointerException |
      ClassCastException | IllegalStateException ignored
    ) {
      return null;
    }
  }

  private static double parseTimeString(String value) {
    if (value == null) return 0;

    value = value.replace("PT", "");
    int minuteEnd = value.indexOf('M');
    String secondsString = value.replace("S", "").substring(minuteEnd + 1);
    String minutesString = value.substring(0, minuteEnd);
    double seconds = 0;

    try { seconds = Double.parseDouble(secondsString); }
    catch (NumberFormatException ignored) {}

    try { seconds += Double.parseDouble(minutesString) * 60; }
    catch (NumberFormatException ignored) {}

    return seconds;
  }
}
