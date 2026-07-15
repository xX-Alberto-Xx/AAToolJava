package org.mcsr.aatool.net.requests;

import java.util.concurrent.CompletableFuture;

public final class AnyPercentRecordRequest extends NetRequest {
  public static final String RANDOM_SEED;
  public static final String SET_SEED;

  private final String subCategory;

  public AnyPercentRecordRequest(boolean rsg) {}

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(String response) {}
}
