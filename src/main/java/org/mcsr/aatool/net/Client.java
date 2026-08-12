package org.mcsr.aatool.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.objectives.Advancement;
import org.mcsr.aatool.utilities.Result;
import org.mcsr.aatool.utilities.Strings;

public final class Client extends Peer {
  private boolean accepted;
  private boolean isConnecting = true;
  private boolean wasKickedByServer;
  private boolean lostConnection;
  private boolean designationsChanged;
  private Instant nextRefresh = Instant.MIN;

  private final Map<String, String> received = new HashMap<>();
  private final Queue<Message> sendQueue = new ArrayDeque<>();

  private InetSocketAddress endPoint;
  private AsynchronousSocketChannel socket;

  public boolean isAccepted() { return this.accepted; }
  public boolean isConnecting() { return this.isConnecting; }
  public boolean wasKickedByServer() { return this.wasKickedByServer; }
  public boolean isConnectionLost() { return this.lostConnection; }
  public boolean areDesignationsChanged() { return this.designationsChanged; }
  public Instant getNextRefresh() { return this.nextRefresh; }

  public static Client tryGet() { return getInstance() instanceof Client client ? client : null; }

  @Override
  public boolean connected() {
    return !this.isConnecting && this.socket != null && isConnected(this.socket) && this.accepted;
  }

  public Result<String> tryGetData(String key) {
    return new Result<>(this.received.containsKey(key), this.received.get(key));
  }

  public String getLongStatusText() {
    User host = this.lobby.tryGetHost();
    String hostname = host != null ? host.getName() : "remote server";

    if (this.lostConnection) return "Lost connection to " + hostname + ". Retrying...";
    if (!this.connected()) return "Attempting connection...";

    long remaining = Instant.now().until(this.nextRefresh, ChronoUnit.SECONDS);

    return remaining > 0
           ? "Synced! Refreshing in " + (
             remaining >= 60
             ? remaining / 60 + " min & " + remaining % 60 + " sec"
             : remaining + " seconds"
           )
           : "Synced with " + hostname + '!';
  }

  public String getShortStatusText() {
    long seconds = (Instant.now().until(this.nextRefresh, ChronoUnit.NANOS) + 999_999_999) / 1_000_000_000;

    if (seconds > 0) {
      return "Refreshing in " + Tracker.getEstimateString(seconds).replace(' ', '\0');
    }

    String hostName = "host";
    Lobby instanceLobby = tryGetLobby();

    if (instanceLobby != null) {
      User host = instanceLobby.tryGetHost();
      if (host != null) hostName = Player.tryGetName(host.id).value;
    }

    return "Syncing with " + hostName;
  }

  @Override
  protected void start(InetAddress address, int port, Uuid id) {
    super.start(address, port, id);

    if (id.equals(Uuid.EMPTY)) {
      this.stop("Error starting client: " + (
        Player.validateName(Config.getNet().minecraftName.getValue())
        ? "Couldn't get UUID. Either Mojang's servers didn't respond or an account with the requested name doesn't exist."
        : "Invalid Minecraft name."
      ));

      return;
    }

    this.endPoint = new InetSocketAddress(address, port);
    this.tryConnect();
  }

  private void tryConnect() { this.tryConnect(false); }
  private void tryConnect(boolean retry) {
    this.accepted = false;

    if (retry) {
      writeToConsole("Reconnecting...");
      updateControls("Reconnecting...", false, false);
    } else {
      writeToConsole("Attempting connection...");
      updateControls("Connecting...", false, false);
    }

    try {
      // Attempt connection
      this.socket = AsynchronousSocketChannel.open();
      Future<Void> f = this.socket.connect(this.endPoint);

      try {
        // Set a timeout for connection attempt
        f.get(Protocol.Peers.CLIENT_CONNECT_MS, TimeUnit.MILLISECONDS);
        this.connectCallback();

        // Start receiving messages from server
        BUFFER.rewind();
        this.socket.read(BUFFER, null, this.receiveCallback);
        this.isConnecting = false;
      } catch (TimeoutException e) {
        // Couldn't establish connection with server
        if (retry) throw e;
        this.stop("Connection Timeout: Couldn't reach server.");
      }
    } catch (IOException | InterruptedException | ExecutionException | TimeoutException exception) {
      if (this.socket != null) {
        try { this.socket.close(); }
        catch (IOException ignored) {}
      }

      if (!(retry && (
        exception instanceof IOException ||
        exception instanceof ExecutionException ||
        exception instanceof TimeoutException
      ))) {
        this.stop("A non-network error has occurred.");
        return;
      }

      updateControls("Stop", true, false);

      try {
        Thread.sleep(Protocol.Peers.CLIENT_RECONNECT_MS);

        if (!this.isDisposing()) {
          this.tryConnect(true);
          return;
        }
      } catch (InterruptedException ignored) {}

      this.stop("Stopped retrying.");
    }
  }

  @Override
  protected void stop(String reason) {
    this.isConnecting = false;

    try {
      if (isConnected(this.socket)) {
        if (!this.wasKickedByServer) {
          // Tell server
          this.sendToServer(Message.logOut());
        }

        // Close connection to server
        this.socket.close();
      }
    } catch (IOException ignored) {}

    super.stop(reason);

    // Re-enable controls
    updateControls("Connect", true, true);
    stateChanged = true;
  }

  public void sendToServer(Message message) {
    if (!isConnected(this.socket)) return;

    // Enqueue message and send immediately if only message in pipeline
    try {
      // Compress and send string to server
      this.socket.write(ByteBuffer.wrap(NetworkHelper.compressString(message.toString())));
    } catch (Exception ignored) {}
  }

  public void sendQueueToServer() {
    if (!isConnected(this.socket)) return;

    // Enqueue message and send immediately if only message in pipeline
    Message queued = this.sendQueue.poll();
    if (queued != null) this.sendToServer(queued);
  }

  public void requestSyncFromServer() {
    // Request sync
    this.sendQueue.add(Message.sync(Protocol.Headers.LOBBY));
    this.sendQueue.add(Message.sync(Protocol.Headers.PROGRESS));
    this.sendQueue.add(Message.sync(Protocol.Headers.REFRESH_ESTIMATE));
    this.sendQueueToServer();
  }

  private void connectCallback() {
    try {
      // Attempt to login to server with user credentials
      User localUser = this.getLocalUser();
      this.sendToServer(Message.logIn(
        localUser.id.string,
        Config.getNet().password.getValue(),
        localUser.pronouns,
        localUser.getName()
      ));
    } catch (Exception ignored) {}
  }

  private final CompletionHandler<Integer, Void> receiveCallback = new CompletionHandler<>() {
    @Override
    public void completed(Integer result, Void v) {
      if (!isConnected(Client.this.socket)) return;

      int length = result;
      if (length <= 0) return;

      byte[] bytes = new byte[length];
      BUFFER.get(0, bytes);
      String content = NetworkHelper.tryDecompressString(bytes);
      if (content == null) return;

      Message message = Message.fromString(content);

      try {
        // Process message
        if (message.isCommand()) Client.this.executeCommand(message);
        else Client.this.copyData(message);

        // Begin receiving again
        if (isConnected(Client.this.socket)) {
          BUFFER.rewind();
          Client.this.socket.read(BUFFER, null, this);
        }

        // Send next queued message if any
        Message queued = Client.this.sendQueue.poll();
        if (queued != null) Client.this.sendToServer(queued);
      } catch (Exception ignored) {
        this.reconnect();
      }
    }

    @Override
    public void failed(Throwable ignored, Void v) {
      if (isConnected(Client.this.socket)) this.reconnect();
    }

    private void reconnect() {
      Client.this.lostConnection = true;
      Client.this.tryConnect(true);
    }
  };

  private void executeCommand(Message message) {
    if (!message.isCommand()) return;

    switch (message.header) {
      case Protocol.Headers.ACCEPT -> {
        // Welcome message
        this.isConnecting = false;

        String serverName = message.tryGetItem(0);
        writeToConsole("Connected to " + (
          Strings.isNullOrEmpty(serverName) ? "remote host." : serverName + "'s Tracker."
        ));

        this.requestSyncFromServer();

        // Enable disconnect button
        updateControls("Disconnect", true, false);
        stateChanged = true;
        this.accepted = true;
        this.lostConnection = false;
      }

      case Protocol.Headers.REFUSE, Protocol.Headers.KICK -> {
        // Connection refused by server / Kicked from server
        this.wasKickedByServer = true;
        String reason = message.tryGetItem(0);
        this.stop(reason != null ? reason : "");
      }
    }
  }

  private void copyData(Message message) {
    if (!message.isData()) return;

    switch (message.header) {
      case Protocol.Headers.LOBBY -> {
        // Deserialize lobby
        this.lobby = Lobby.fromJsonString(message.tryGetItem(0));
        stateChanged = true;
        syncUserList(this.lobby.users.values());

        for (Map.Entry<String, Uuid> designation : this.lobby.designations.entrySet()) {
          Result<Advancement> advancement = Tracker.tryGetAdvancement(designation.getKey());

          if (advancement.success && advancement.value.isDesignationLinked()) {
            advancement.value.designate(designation.getValue());
          }
        }
      }

      case Protocol.Headers.REFRESH_ESTIMATE -> {
        // Deserialize datetime
        String dateString = message.tryGetItem(0);
        if (dateString == null) break;

        try {
          this.nextRefresh = LocalDateTime.parse(dateString, Message.REFRESH_ESTIMATE_FORMAT)
            .toInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException ignored) {}
      }

      case Protocol.Headers.BLOCK_HIGHLIGHTS -> {
        if (!(Tracker.getCategory() instanceof AllBlocks ab)) break;

        ab.clearHighlighted();
        ab.clearConfirmed();
        // Deserialize all blocks highlights
        String list = message.tryGetItem(0);
        ab.applyChecklist(list != null ? list.split("\n") : new String[] { "" });
      }

      default -> {
        String jsonString = message.tryGetItem(0);
        if (jsonString == null) break;

        // Deserialize progress
        this.received.put(message.header, jsonString);
        stateChanged = true;
      }
    }

    writeToConsole("Received " + message.header + " from server.");
  }
}
