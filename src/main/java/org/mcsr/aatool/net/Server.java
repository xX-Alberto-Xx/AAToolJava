package org.mcsr.aatool.net;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class Server extends Peer {
  private static final Map<String, Uuid> PREPARED_DESIGNATIONS;

  private final Map<String, Socket> clients;
  private final Map<Socket, User> users;
  private final Socket listener;
  private final String password;

  private boolean isStopping;

  public Server() {}

  public static void prepareDesignation(String advancement, Uuid player) {}

  public static boolean tryGet(/*out */Server server) {}

  @Override
  public boolean connected() {}

  public void sendToClient(Socket client, Message message) {}

  public void sendToAllClients(Message message) {}

  public void sendLobby(Socket client/* = null*/) {}

  public void sendBlockHighlights(Socket client/* = null*/) {}

  public void sendProgress(Socket client/* = null*/) {}

  public void sendNextRefresh(Socket client/* = null*/) {}

  public void designatePlayer(String advancement, Uuid player) {}

  public void kickPlayer(Uuid id) {}

  @Override
  protected void start(IPAddress address, int port, Uuid id) {}

  private void acceptCallback(IAsyncResult ar) {}

  private void sendCallback(IAsyncResult ar) {}

  private void receiveCallback(IAsyncResult ar) {}

  @Override
  protected void stop(String reason) {}

  private static boolean passwordsAreEqual(String a, String b) {}

  private boolean tryLogIn(User user, String password, String protocolVersion, Socket client) {}

  private void logOut(Socket client, String reason, boolean clientRequested/* = false*/) {}

  private CompletableFuture<Void> executeCommandAsync(Message message, Socket sender) {}
}
