package org.mcsr.aatool.net.requests;

import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.net.Uuid;

public final class AvatarRequest extends NetRequest {
  private static int downloads;

  private final Uuid id;
  private final String name;
  private final boolean isFallback;

  public AvatarRequest(Uuid player, boolean isFallback/* = false*/) {}

  public AvatarRequest(String name) {}

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(Stream avatarStream) {}

  private static void saveToCache(Texture2D texture, String fileName) {}
}
