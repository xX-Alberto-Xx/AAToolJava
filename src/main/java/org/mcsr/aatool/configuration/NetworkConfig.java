package org.mcsr.aatool.configuration;

import org.mcsr.aatool.net.Protocol;

public class NetworkConfig extends Config {
  public final Setting<String> minecraftName = new Setting<>("");
  public final Setting<String> preferredName = new Setting<>("");
  public final Setting<String> pronouns = new Setting<>("");

  public final Setting<String> password = new Setting<>("");
  public final Setting<String> ip = new Setting<>("");
  public final Setting<Integer> port = new Setting<>(Protocol.Peers.DEFAULT_PORT);

  public final Setting<Boolean> autoServerIP = new Setting<>(true);
  public final Setting<Boolean> isServer = new Setting<>(false);

  public NetworkConfig() {
    this.registerSetting(this.minecraftName);
    this.registerSetting(this.preferredName);
    this.registerSetting(this.pronouns);
    this.registerSetting(this.password);
    this.registerSetting(this.ip);
    this.registerSetting(this.port);
    this.registerSetting(this.autoServerIP);
    this.registerSetting(this.isServer);
  }

  @Override
  protected String getId() { return "network"; }
}
