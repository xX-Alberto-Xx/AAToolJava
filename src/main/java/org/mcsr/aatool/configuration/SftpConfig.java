package org.mcsr.aatool.configuration;

public class SftpConfig extends Config {
  public final Setting<String> host = new Setting<>("");
  public final Setting<String> username = new Setting<>("");
  public final Setting<String> password = new Setting<>("");
  public final Setting<Integer> port = new Setting<>(22);
  public final Setting<Integer> autoSaveMinutes = new Setting<>(5);
  public final Setting<Boolean> linux = new Setting<>(false);

  public final Setting<String> serverRoot = new Setting<>("");

  public SftpConfig() {
    this.registerSetting(this.host);
    this.registerSetting(this.username);
    this.registerSetting(this.password);
    this.registerSetting(this.port);
    this.registerSetting(this.serverRoot);
  }

  @Override
  protected String getId() { return "sftp"; }
}
