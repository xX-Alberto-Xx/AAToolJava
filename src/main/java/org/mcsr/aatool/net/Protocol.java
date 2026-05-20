package org.mcsr.aatool.net;

public final class Protocol {
  public static final Version VERSION;

  public static final int BUFFER_SIZE;
  public static final char COMMAND_PREFIX;
  public static final char DATA_PREFIX;
  public static final char TOKEN_DELIMITER;
  public static final String HOST_KEY;

  private Protocol() {}

  public static final class Peers {
    public static final int CLIENT_CONNECT_MS;
    public static final int CLIENT_RECONNECT_MS;
    public static final int SERVER_CAPACITY;
    public static final int SERVER_BACKLOG;
    public static final int DEFAULT_PORT;

    private Peers() {}
  }

  public static final class Requests {
    public static final int MAX_CONCURRENT;
    public static final int MAX_RETRIES;

    public static final int TIMEOUT_NORMAL_MS;
    public static final int TIMEOUT_LONGER_MS;
    public static final double UPDATE_RATE;
    public static final double RETRY_COOLDOWN;

    private Requests() {}
  }

  public static final class Headers {
    public static final String LOGIN;
    public static final String LOGOUT;
    public static final String SYNC;

    public static final String ACCEPT;
    public static final String REFUSE;
    public static final String KICK;

    public static final String PROGRESS;
    public static final String LOBBY;
    public static final String REFRESH_ESTIMATE;
    public static final String BLOCK_HIGHLIGHTS;

    private Headers() {}
  }
}
