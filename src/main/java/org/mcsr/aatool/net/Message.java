package org.mcsr.aatool.net;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

public class Message {
  public static final DateTimeFormatter REFRESH_ESTIMATE_FORMAT =
    DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm:ss", Locale.ROOT);
  public static final Message EMPTY = new Message('\0', "");

  public final char prefix;
  public final String header;
  public final String[] items;

  private final String stringRepresentation;

  private Message(char prefix, String header, String... items) {
    this.prefix = prefix;
    this.header = header;
    this.items = items;
    this.stringRepresentation = prefix + header + Protocol.TOKEN_DELIMITER
                              + String.join(Protocol.TOKEN_DELIMITER, items);
  }

  public static Message fromString(String message) {
    if (message == null) return EMPTY;

    String[] tokens = message.split(Protocol.TOKEN_DELIMITER);
    return tokens.length > 0 && tokens[0].length() > 1 ? new Message(
      tokens[0].charAt(0),
      tokens[0].substring(1),
      Arrays.copyOfRange(tokens, 1, tokens.length)
    ) : EMPTY;
  }

  public static Message logIn(String uuid, String password, String pronouns, String displayName) {
    return newCommand(Protocol.Headers.LOGIN, Protocol.VERSION.toString(), uuid, password, pronouns, displayName);
  }

  public static Message logOut() { return newCommand(Protocol.Headers.LOGOUT); }
  public static Message sync(String type) { return newCommand(Protocol.Headers.SYNC, type); }
  public static Message accept(String serverHostName) { return newCommand(Protocol.Headers.ACCEPT, serverHostName); }
  public static Message refuse(String reason) { return newCommand(Protocol.Headers.REFUSE, reason); }
  public static Message kick(String reason) { return newCommand(Protocol.Headers.KICK, reason); }
  public static Message progress(String jsonString) { return newData(Protocol.Headers.PROGRESS, jsonString); }
  public static Message lobby(String jsonString) { return newData(Protocol.Headers.LOBBY, jsonString); }
  public static Message blockHighlights(String list) { return newData(Protocol.Headers.BLOCK_HIGHLIGHTS, list); }

  public static Message sftpEstimate(Instant nextRefresh) {
    return newData(
      Protocol.Headers.REFRESH_ESTIMATE,
      nextRefresh.atOffset(ZoneOffset.UTC).format(REFRESH_ESTIMATE_FORMAT)
    );
  }

  private static Message newCommand(String header, String... items) {
    return new Message(Protocol.COMMAND_PREFIX, header, items);
  }

  private static Message newData(String header, String data) {
    return new Message(Protocol.DATA_PREFIX, header, data);
  }

  public final boolean isCommand() { return this.prefix == Protocol.COMMAND_PREFIX; }
  public final boolean isData() { return this.prefix == Protocol.DATA_PREFIX; }
  public final boolean isEmpty() { return this.equals(EMPTY); }

  @Override
  public String toString() { return this.stringRepresentation; }

  public final String tryGetItem(int index) {
    return index >= 0 && index < this.items.length ? this.items[index] : null;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Message message
        && this.prefix == message.prefix
        && this.header.equals(message.header)
        && Arrays.equals(this.items, message.items);
  }

  @Override
  public int hashCode() {
    return (
      (
        -600507736 * -1521134295 + this.prefix
      ) * -1521134295 + this.header.hashCode()
    ) * -1521134295 + Arrays.hashCode(this.items);
  }
}
