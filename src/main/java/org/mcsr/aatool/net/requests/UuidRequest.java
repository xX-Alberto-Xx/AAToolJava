package org.mcsr.aatool.net.requests;

import java.util.concurrent.CompletableFuture;

public final class UuidRequest extends NetRequest {
  private static int downloads;

  private final String name;
  private final boolean requestAvatar;

  public UuidRequest(String name, boolean requestAvatar/* = false*/) {}

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(String response) {}
}
