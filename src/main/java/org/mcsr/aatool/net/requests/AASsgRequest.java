package org.mcsr.aatool.net.requests;

import java.util.concurrent.CompletableFuture;

public final class AASsgRequest extends NetRequest {
  public static final String SET_SEED;

  public AASsgRequest() {}

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(String response) {}
}
