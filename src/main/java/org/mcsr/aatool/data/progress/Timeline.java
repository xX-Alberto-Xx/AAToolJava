package org.mcsr.aatool.data.progress;

import java.util.List;
import java.util.Map;

public class Timeline {
  public final List<Event> events;
  private Map<String, Integer> currentPickups;
  private String currentSaveName;

  public Timeline() {}

  private String getCurrentTimelineFile() {}

  public final void updateState(WorldState state) {}

  private void updatePickups(WorldState state) {}

  public final void tryLoad() {}

  public final void trySave() {}

  public static class Event {
    public String label;
    public DateTime when;

    public Event(String label, DateTime when) {}
  }
}
