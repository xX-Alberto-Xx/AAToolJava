package org.mcsr.aatool.configuration;

public class NotesConfig extends Config {
  public final Setting<Boolean> enabled;
  public final Setting<Boolean> alwaysOnTop;

  public final Setting<Integer> width;
  public final Setting<Integer> height;

  public NotesConfig() {}

  @Override
  protected String getId() {}
}
