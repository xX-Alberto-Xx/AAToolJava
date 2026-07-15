package org.mcsr.aatool.net;

import java.util.List;

public abstract class Peer {
  private static Peer instance;
  protected static boolean stateChanged;

  protected static final byte[] BUFFER;
  private static final List<NetworkController> CONTROLLERS;

  protected Lobby lobby;
  private User localUser;
  private User host;
  private String address;
  private String port;
  private String consoleOutput;
  private boolean isDisposing;

  public Peer() {}

  public static Peer getInstance() { return instance; }
  public static boolean isStateChanged() { return stateChanged; }

  public static boolean isClient() {}
  public static boolean isServer() {}
  public static boolean isRunning() {}
  public static boolean isConnected() {}

  public static void clearFlags() {}

  public static boolean tryGetLobby(/*out */Lobby lobby) {}

  public static <T extends Peer> void startInstanceOf(String ip, String port) {}

  public static void stopInstance() {}

  public static void updateControls(String buttonText, boolean enableButton, boolean enableDropDown) {}

  public static void bindController(NetworkController controller) {}

  public static void unbindController(NetworkController controller) {}

  public static void writeToConsole(String line) {}

  public static void syncConsole() {}

  public static void syncUserList(Iterable<User> users) {}

  public final User getLocalUser() { return this.localUser; }
  public final User getHost() { return this.host; }
  public final String getAddress() { return this.address; }
  public final String getPort() { return this.port; }
  public final String getConsoleOutput() { return this.consoleOutput; }
  public final boolean isDisposing() { return this.isDisposing; }

  public abstract boolean connected();

  protected void start(IPAddress address, int port, Uuid id) {}

  protected void stop(String reason) {}
}
