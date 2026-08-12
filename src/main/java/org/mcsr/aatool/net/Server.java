package org.mcsr.aatool.net;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.progress.NetworkState;
import org.mcsr.aatool.net.requests.NameRequest;
import org.mcsr.aatool.saves.MinecraftServer;
import org.mcsr.aatool.utilities.Strings;
import org.mcsr.aatool.utilities.Version;

public final class Server extends Peer {
  private static final Map<String, Uuid> PREPARED_DESIGNATIONS = new HashMap<>();

  private final Map<String, AsynchronousSocketChannel> clients = new HashMap<>();
  private final Map<AsynchronousSocketChannel, User> users = new HashMap<>();
  private final AsynchronousServerSocketChannel listener;
  private final byte[] passwordBytes;

  private boolean isStopping;

  public Server() throws IOException {
    this.listener = AsynchronousServerSocketChannel.open();
    String password = Config.getNet().password.getValue();
    this.passwordBytes = !Strings.isNullOrEmpty(password) ? password.getBytes(StandardCharsets.UTF_8) : null;

    for (Map.Entry<String, Uuid> designation : PREPARED_DESIGNATIONS.entrySet()) {
      this.designatePlayer(designation.getKey(), designation.getValue());
    }
  }

  public static void prepareDesignation(String advancement, Uuid player) {
    PREPARED_DESIGNATIONS.put(advancement, player);
  }

  public static Server tryGet() { return getInstance() instanceof Server server ? server : null; }

  @Override
  public boolean connected() {
    try { return this.listener.getLocalAddress() != null; }
    catch (IOException ignored) { return false; }
  }

  public void sendToClient(AsynchronousSocketChannel client, Message message) {
    // Make sure socket exists
    if (!(this.connected() && isConnected(client))) return;

    // Start sending bytes to client socket
    try {
      client.write(ByteBuffer.wrap(NetworkHelper.compressString(message.toString())));
    } catch (Exception ignored) {}
  }

  public void sendToAllClients(Message message) {
    // Send message to all connected client sockets
    for (AsynchronousSocketChannel client : this.clients.values()) {
      this.sendToClient(client, message);
    }
  }

  public void sendLobby() { this.sendLobby(null); }
  public void sendLobby(AsynchronousSocketChannel client) {
    // Clients never receive and are completely oblivious to everyone else's IP addresses
    // Send lobby state to client(s)
    this.sendTo(client, Message.lobby(this.lobby.toJsonString()));
  }

  public void sendBlockHighlights() { this.sendBlockHighlights(null); }
  public void sendBlockHighlights(AsynchronousSocketChannel client) {
    if (Tracker.getCategory() instanceof AllBlocks ab) {
      // Send block highlights to client(s)
      this.sendTo(client, Message.blockHighlights(ab.getBlockHighlights()));
    }
  }

  public void sendProgress() { this.sendProgress(null); }
  public void sendProgress(AsynchronousSocketChannel client) {
    // Send progress state to client(s)
    this.sendTo(client, Message.progress(new NetworkState(Tracker.getState()).toJsonString()));
  }

  public void sendNextRefresh() { this.sendNextRefresh(null); }
  public void sendNextRefresh(AsynchronousSocketChannel client) {
    // Send next refresh estimate to client(s)
    this.sendTo(client, Message.sftpEstimate(MinecraftServer.getRefreshEstimate()));
  }

  private void sendTo(AsynchronousSocketChannel client, Message message) {
    if (client == null) this.sendToAllClients(message);
    else this.sendToClient(client, message);
  }

  public void designatePlayer(String advancement, Uuid player) {
    if (!player.equals(this.lobby.designations.get(advancement))) {
      this.lobby.designations.put(advancement, player);
      this.sendLobby();
    }
  }

  public void kickPlayer(Uuid id) {
    AsynchronousSocketChannel client = null;

    for (Map.Entry<AsynchronousSocketChannel, User> user : this.users.entrySet()) {
      if (id.equals(user.getValue().id)) {
        client = user.getKey();
        break;
      }
    }

    if (client != null) this.logOut(client, "forcibly kicked by host.");
  }

  @Override
  protected void start(InetAddress address, int port, Uuid id) {
    super.start(address, port, id);

    // Add host to lobby
    if (!id.equals(Uuid.EMPTY)) {
      this.lobby.setHost(this.getLocalUser());
      syncUserList(this.lobby.users.values());
    } else if (!this.getLocalUser().equals(User.NOBODY)) {
      this.stop("Failed: " + (
        Player.validateName(Config.getNet().minecraftName.getValue())
        ? "Unknown network error."
        : "Invalid Minecraft name."
      ));
      return;
    }

    try {
      // Create TCP listening socket and attempt binding
      this.listener.bind(new InetSocketAddress(address, port), Protocol.Peers.SERVER_BACKLOG);

      // Start accepting clients
      this.listener.accept(null, this.acceptCallback);
      writeToConsole("Started server.");
      writeToConsole("Awaiting connections...");

      // Disable controls
      updateControls("Stop", true, false);
      stateChanged = true;
    } catch (IOException | IllegalStateException | IllegalArgumentException e) {
      this.stop("Error starting server: " + e.getMessage() + '.');
    }
  }

  private final CompletionHandler<AsynchronousSocketChannel, Void> acceptCallback = new CompletionHandler<>() {
    @Override
    public void completed(AsynchronousSocketChannel client, Void v) {
      if (Server.this.isStopping) return;

      try {
        BUFFER.rewind();
        client.read(BUFFER, client, Server.this.receiveCallback);
      } finally {
        this.listenAgain();
      }
    }

    @Override
    public void failed(Throwable e, Void v) {
      if (Server.this.isStopping) return;

      this.listenAgain();
      throw new RuntimeException(e);
    }

    private void listenAgain() {
      // Listen for next connection attempt
      try { Server.this.listener.accept(null, this); }
      catch (Exception ignored) {}
    }
  };

  private final CompletionHandler<Integer, AsynchronousSocketChannel> receiveCallback = new CompletionHandler<>() {
    @Override
    public void completed(Integer result, AsynchronousSocketChannel client) {
      int length = result;

      if (length <= 0) {
        this.disconnect(client);
        return;
      }

      // Reconstruct message
      byte[] bytes = new byte[length];
      BUFFER.get(0, bytes);
      String content = NetworkHelper.tryDecompressString(bytes);
      if (content == null) return;

      Message message = Message.fromString(content);

      try {
        // Process message
        if (message.isCommand()) Server.this.executeCommandAsync(message, client);

        // Start receiving again
        if (isConnected(client)) {
          BUFFER.rewind();
          client.read(BUFFER, client, this);
        }
      } catch (Exception ignored) {
        this.disconnect(client);
      }
    }

    @Override
    public void failed(Throwable ignored, AsynchronousSocketChannel client) {
      this.disconnect(client);
    }

    private void disconnect(AsynchronousSocketChannel client) {
      Server.this.logOut(client, "lost connection.");
    }
  };

  @Override
  protected void stop(String reason) {
    this.isStopping = true;

    // Close listener socket
    try { this.listener.close(); }
    catch (IOException e) { throw new UncheckedIOException(e); }

    // Disconnect clients
    int total = this.clients.size();

    if (total > 0) {
      for (AsynchronousSocketChannel client : this.clients.values().toArray(AsynchronousSocketChannel[]::new)) {
        this.logOut(client, "Disconnected. (Server stopped)");
        this.sendToClient(client, Message.kick(reason));
      }

      writeToConsole("Disconnected " + total + " client" + (total == 1 ? "" : "s") + '.');
    }

    super.stop(reason);
    updateControls("Host", true, true);
    stateChanged = true;
  }

  private boolean passwordMatches(String password) {
    // Actual time-constant password comparison (prevents timing attacks)
    return MessageDigest.isEqual(password.getBytes(StandardCharsets.UTF_8), this.passwordBytes);
  }

  private boolean tryLogIn(User user, String password, String protocolVersion, AsynchronousSocketChannel client) {
    // Validate user credentials
    String problem = null;
    String remoteKey = null;

    if (!Protocol.VERSION.equals(Version.tryParse(protocolVersion))) {
      problem = "Client AATool version is not supported.";
    } else if (this.passwordBytes != null && !this.passwordMatches(password)) {
      problem = "Incorrect password.";
    } else {
      boolean ipTaken;

      try {
        InetSocketAddress remote = (InetSocketAddress) client.getRemoteAddress();
        remoteKey = remote.getAddress().getHostAddress() + ':' + remote.getPort();
        ipTaken = this.clients.containsKey(remoteKey);
      } catch (IOException ignored) {
        ipTaken = true;
      }

      if (ipTaken) {
        problem = "IP conflict.";
      } else if (this.lobby.users.containsKey(user.id)) {
        problem = "Minecraft username already taken.";
      } else {
        String name = user.getName();
        boolean nameTaken = false;

        for (User player : this.lobby.users.values()) {
          if (player.getName().equals(name)) {
            nameTaken = true;
            break;
          }
        }

        if (nameTaken) {
          problem = "Preferred username already taken.";
        } else if (this.clients.size() == Protocol.Peers.SERVER_CAPACITY) {
          problem = "Server full.";
        }
      }
    }

    if (problem != null) {
      // Refuse connection due to invalid user credentials
      problem = "Connection Refused: " + problem;
      this.sendToClient(client, Message.refuse(problem));
      writeToConsole(problem);
      return false;
    }

    // Update currently connected clients
    this.lobby.add(user);
    this.sendLobby();

    // Add client socket to list
    this.users.put(client, user);
    this.clients.put(remoteKey, client);

    // Log to console
    writeToConsole(user.getName() + " connected!");
    syncUserList(this.lobby.users.values());

    // Notify client that they have been accepted
    this.sendToClient(client, Message.accept(this.getLocalUser().getName()));
    return true;
  }

  private void logOut(AsynchronousSocketChannel client, String reason) { this.logOut(client, reason, false); }
  private void logOut(AsynchronousSocketChannel client, String reason, boolean clientRequested) {
    if (this.clients.isEmpty() || this.isStopping) return;

    // Log to console
    User user = this.users.get(client);
    writeToConsole(user != null ? user.getName() + ' ' + reason : reason);

    if (isConnected(client)) {
      try (client) {
        // Tell client to disconnect and dispose of socket
        if (!clientRequested) this.sendToClient(client, Message.kick(reason));
      } catch (IOException ignored) {}
    }

    // Clear out client lists and lobby
    this.clients.values().remove(client);
    this.users.remove(client);
    this.lobby.remove(user);
    syncUserList(this.lobby.users.values());
    this.sendLobby();
  }

  private void executeCommandAsync(Message message, AsynchronousSocketChannel sender) {
    switch (message.header) {
      case Protocol.Headers.LOGIN -> {
        Uuid id = Uuid.tryParse(message.tryGetItem(1));

        if (id == null) {
          writeToConsole("Connection attempt refused: Malformed UUID.");
          this.sendToClient(sender, Message.refuse("Malformed UUID"));
          return;
        }

        new NameRequest(id).downloadAsync().thenAccept(success -> {
          if (!success) {
            writeToConsole("Connection attempt refused: Couldn't validate name with Mojang.");
            this.sendToClient(sender, Message.refuse("Couldn't validate name with Mojang. Try re-connecting."));
            return;
          }

          // Register new user
          String versionNumber = message.tryGetItem(0);
          String password = message.tryGetItem(2);
          String pronouns = message.tryGetItem(3);
          String displayName = message.tryGetItem(4);

          if (this.tryLogIn(
            new User(id, pronouns != null ? pronouns : "", displayName != null ? displayName : ""),
            password != null ? password : "",
            versionNumber != null ? versionNumber : "",
            sender
          )) {
            Player.fetchIdentityAsync(id);
          }
        });
      }

      case Protocol.Headers.LOGOUT -> {
        // Remove player from lobby
        this.logOut(sender, "disconnected.", true);
      }

      case Protocol.Headers.SYNC -> {
        // Send expected type of data to all clients
        switch (message.tryGetItem(0)) {
          case Protocol.Headers.LOBBY -> this.sendLobby(sender);
          case Protocol.Headers.PROGRESS -> this.sendProgress(sender);
          case Protocol.Headers.REFRESH_ESTIMATE -> this.sendNextRefresh(sender);
        }
      }
    }
  }
}
