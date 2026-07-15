package org.mcsr.aatool.net.requests;

import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.net.Uuid;

public final class NameRequest extends NetRequest {
  private static int downloads;

  private final Uuid id;
  private final String shortId;

  public NameRequest(Uuid id) {}

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(String response) {}
}
