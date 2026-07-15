package org.mcsr.aatool.net;

public class Message {
  public static final Message EMPTY;

  public final char prefix;
  public final String header;
  public final String[] items;

  private final String stringRepresentation;

  private Message(char prefix, String header, /*params */String[] items) {}

  public static Message fromString(String message) {}

  public static Message logIn(String uuid, String password, String pronouns, String displayName) {}
  public static Message logOut() {}
  public static Message sync(String type) {}
  public static Message accept(String serverHostName) {}
  public static Message refuse(String reason) {}
  public static Message kick(String reason) {}
  public static Message progress(String jsonString) {}
  public static Message lobby(String jsonString) {}
  public static Message blockHighlights(String list) {}
  public static Message sftpEstimate(DateTime nextRefresh) {}

  private static Message newCommand(String header, /*params */String[] items) {}
  private static Message newData(String header, String data) {}

  public final boolean isCommand() {}
  public final boolean isData() {}
  public final boolean isEmpty() {}

  @Override
  public String toString() {}

  public final boolean tryGetItem(int index, /*out */String item) {}

  @Override
  public boolean equals(Object obj) {}

  @Override
  public int hashCode() {}
}
