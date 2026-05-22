package org.mcsr.aatool.configuration;

public class NotesConfig extends Config {
  public final Setting<Boolean> enabled = new Setting<>(false);
  public final Setting<Boolean> alwaysOnTop = new Setting<>(true);

  public final Setting<Integer> width = new Setting<>(420);
  public final Setting<Integer> height = new Setting<>(420);

  public NotesConfig() {
    this.registerSetting(this.enabled);
    this.registerSetting(this.alwaysOnTop);
    this.registerSetting(this.width);
    this.registerSetting(this.height);
  }

  @Override
  protected String getId() { return "notes"; }
}
