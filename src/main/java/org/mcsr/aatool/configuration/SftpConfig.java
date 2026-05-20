package org.mcsr.aatool.configuration;

public class SftpConfig extends Config {
  public final Setting<String> host;
  public final Setting<String> username;
  public final Setting<String> password;
  public final Setting<Integer> port;
  public final Setting<Integer> autoSaveMinutes;
  public final Setting<Boolean> linux;

  public final Setting<String> serverRoot;

  public SftpConfig() {}

  @Override
  protected String getId() {}
}
