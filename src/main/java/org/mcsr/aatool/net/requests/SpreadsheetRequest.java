package org.mcsr.aatool.net.requests;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.utilities.Pair;

public final class SpreadsheetRequest extends NetRequest {
  private static int downloads;

  public static Set<Pair<String, String>> downloadedPages;

  private final String sheet;
  private final String page;
  private final String name;

  public SpreadsheetRequest(String name, String sheet, String page/* = "0"*/) {}

  public static int getDownloads() { return downloads; }

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(String csv) {}
}
