package org.mcsr.aatool.configuration;

public class NetworkConfig extends Config {
  public final Setting<String> minecraftName;
  public final Setting<String> preferredName;
  public final Setting<String> pronouns;

  public final Setting<String> password;
  public final Setting<String> ip;
  public final Setting<Integer> port;

  public final Setting<Boolean> autoServerIP;
  public final Setting<Boolean> isServer;

  public NetworkConfig() {}

  @Override
  protected String getId() {}
}
