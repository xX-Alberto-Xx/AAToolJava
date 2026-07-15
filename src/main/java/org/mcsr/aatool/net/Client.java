package org.mcsr.aatool.net;

import java.util.Map;
import java.util.Queue;

public final class Client extends Peer {
  private boolean accepted;
  private boolean isConnecting;
  private boolean wasKickedByServer;
  private boolean lostConnection;
  private boolean designationsChanged;
  private DateTime nextRefresh;

  private final Map<String, String> received;
  private final Queue<Message> sendQueue;

  private IPEndPoint endPoint;
  private Socket socket;

  public Client() {}

  public boolean isAccepted() { return this.accepted; }
  public boolean isConnecting() { return this.isConnecting; }
  public boolean isWasKickedByServer() { return this.wasKickedByServer; }
  public boolean isLostConnection() { return this.lostConnection; }
  public boolean isDesignationsChanged() { return this.designationsChanged; }
  public DateTime getNextRefresh() { return this.nextRefresh; }

  public static boolean tryGet(/*out */Client client) {}

  @Override
  public boolean connected() {}

  public boolean tryGetData(String key, /*out */String data) {}

  public String getLongStatusText() {}

  public String getShortStatusText() {}

  @Override
  protected void start(IPAddress address, int port, Uuid id) {}

  private void tryConnect(boolean retry/* = false*/) {}

  @Override
  protected void stop(String reason) {}

  public void sendToServer(Message message) {}

  public void sendQueueToServer() {}

  public void requestSyncFromServer() {}

  private void connectCallback(IAsyncResult ar) {}

  private void sendCallback(IAsyncResult ar) {}

  private void receiveCallback(IAsyncResult ar) {}

  private void executeCommand(Message message) {}

  private void copyData(Message message) {}
}
