package org.mcsr.aatool.net;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.configuration.NetworkConfig;
import org.mcsr.aatool.utilities.Strings;

public abstract class Peer {
  private static Peer instance;
  protected static boolean stateChanged;

  protected static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(Protocol.BUFFER_SIZE);
  private static final List<NetworkController> CONTROLLERS = new ArrayList<>();

  protected Lobby lobby = new Lobby();
  private User localUser;
  private String address;
  private String port;
  private String consoleOutput;
  private boolean isDisposing;

  public static Peer getInstance() { return instance; }
  public static boolean isStateChanged() { return stateChanged; }

  public static boolean isClient() { return instance instanceof Client; }
  public static boolean isServer() { return instance instanceof Server; }
  public static boolean isRunning() { return instance != null; }
  public static boolean isConnected() { return instance != null && instance.connected(); }

  public static void clearFlags() { stateChanged = false; }

  public static Lobby tryGetLobby() { return instance != null ? instance.lobby : null; }

  public static <T extends Peer> void startInstanceOf(String ip, String port, Class<T> classOfT) {
    // Make sure not already running
    if (isRunning()) stopInstance();

    // Make sure name is valid
    NetworkConfig netConfig = Config.getNet();
    String name = netConfig.minecraftName.getValue();
    boolean requiresUser = classOfT == Client.class || !Strings.isNullOrEmpty(name);

    if (requiresUser && !Player.validateName(name)) {
      // TODO: UI
      return;
    }

    // Make sure IP is valid or can be resolved
    InetAddress ipAddress;

    try {
      ipAddress = InetAddress.getByName(ip);
    } catch (UnknownHostException ignored) {
      // TODO: UI
      return;
    }

    // Make sure port is valid
    int portNumber;

    try {
      portNumber = Integer.parseInt(port);
    } catch (NumberFormatException ignored) {
      // TODO: UI
      return;
    }

    // Warn user if they are about to host without a password
    if (classOfT == Server.class && Strings.isNullOrEmpty(netConfig.password.getValue())) {
      // TODO: UI
    }

    // Start peer instance as client or server without blocking main thread
    Thread instanceThread = new Thread(() -> {
      Uuid id = Uuid.EMPTY;

      if (requiresUser) {
        writeToConsole("Getting UUID from Mojang...");
        updateControls("Getting UUID...", false, false);
        id = Player.fetchUuidAsync(name).join();
      }

      try {
        try {
          instance = classOfT.getConstructor().newInstance();
        } catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
          if (e instanceof InvocationTargetException && e.getCause() instanceof Exception cause) throw cause;
          throw new AssertionError(classOfT.getSimpleName() + " cannot be instantiated", e);
        }

        instance.start(ipAddress, portNumber, id);
      } catch (Exception ignored) {
        writeToConsole("Failed to initialize. Try again.");
        updateControls(classOfT == Client.class ? "Connect" : "Host", true, true);
      }
    });

    instanceThread.setDaemon(true);
    instanceThread.start();
  }

  public static void stopInstance() {
    if (instance instanceof Client) instance.stop("Disconnected.");
    else if (instance instanceof Server) instance.stop("Server closed.");
  }

  public static void updateControls(String buttonText, boolean enableButton, boolean enableDropDown) {
    for (NetworkController controller : CONTROLLERS) {
      if (controller != null) {
        controller.setControlStates(buttonText, enableButton, enableDropDown);
      }
    }
  }

  public static void bindController(NetworkController controller) {
    CONTROLLERS.add(controller);
    syncConsole();
    if (isRunning()) syncUserList(instance.lobby.users.values());
  }

  public static void unbindController(NetworkController controller) {
    CONTROLLERS.remove(controller);
  }

  public static void writeToConsole(String line) {
    for (NetworkController controller : CONTROLLERS) {
      try {
        if (controller != null) controller.writeToConsole(line);
      } catch (Exception ignored) {}
    }

    if (instance != null) instance.writeToConsoleOutput(line);
  }

  public static void syncConsole() {
    for (NetworkController controller : CONTROLLERS) {
      try {
        if (controller != null) controller.syncConsole();
      } catch (Exception ignored) {}
    }
  }

  public static void syncUserList(Iterable<User> users) {
    for (NetworkController controller : CONTROLLERS) {
      try {
        if (controller != null) controller.syncUserList(users);
      } catch (Exception ignored) {}
    }
  }

  public final User getLocalUser() { return this.localUser; }
  public final String getAddress() { return this.address; }
  public final String getPort() { return this.port; }
  public final String getConsoleOutput() { return this.consoleOutput; }
  public final boolean isDisposing() { return this.isDisposing; }

  private void writeToConsoleOutput(String line) { this.consoleOutput += line + '\n'; }

  public abstract boolean connected();

  protected void start(InetAddress address, int port, Uuid id) {
    this.address = address.getHostAddress();
    this.port = Integer.toString(port);

    NetworkConfig netConfig = Config.getNet();
    this.localUser = new User(id, netConfig.pronouns.getValue(), netConfig.preferredName.getValue());
  }

  protected void stop(String reason) {
    instance = null;
    this.isDisposing = true;
    writeToConsole(reason);
    syncUserList(Set.of());
  }

  protected static boolean isConnected(AsynchronousSocketChannel socket) {
    try { return socket.getRemoteAddress() != null; }
    catch (IOException ignored) { return false; }
  }
}
