package org.mcsr.aatool.net;

import org.mcsr.aatool.utilities.Version;

public final class Protocol {
  public static final Version VERSION = new Version(11, 0);

  public static final int BUFFER_SIZE = 1024 * 1000;
  public static final char COMMAND_PREFIX = '/';
  public static final char DATA_PREFIX = '$';
  public static final char TOKEN_DELIMITER = '\n';
  public static final String HOST_KEY = "$host";

  private Protocol() {}

  public static final class Peers {
    public static final int CLIENT_CONNECT_MS = 10 * 1000;
    public static final int CLIENT_RECONNECT_MS = 3 * 1000;
    public static final int SERVER_CAPACITY = 30;
    public static final int SERVER_BACKLOG = 3;
    public static final int DEFAULT_PORT = 25562;

    private Peers() {}
  }

  public static final class Requests {
    public static final int MAX_CONCURRENT = 3;
    public static final int MAX_RETRIES = 2;

    public static final int TIMEOUT_NORMAL_MS = 10 * 1000;
    public static final int TIMEOUT_LONGER_MS = 20 * 1000;
    public static final double UPDATE_RATE = 0.25;
    public static final double RETRY_COOLDOWN = 10 * 60 * 1000;

    private Requests() {}
  }

  public static final class Headers {
    // Client to server command headers
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String SYNC = "sync";

    // Server to client command headers
    public static final String ACCEPT = "accept";
    public static final String REFUSE = "refuse";
    public static final String KICK = "kick";

    // Server to client data headers
    public static final String PROGRESS = "progress";
    public static final String LOBBY = "lobby";
    public static final String REFRESH_ESTIMATE = "refresh estimate";
    public static final String BLOCK_HIGHLIGHTS = "block highlights";

    private Headers() {}
  }
}
