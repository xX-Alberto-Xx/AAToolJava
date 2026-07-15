package org.mcsr.aatool.net.requests;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Version;

public final class UpdateRequest extends NetRequest {
  private static final String PATCH_NOTES_URL;
  private static final String THUMBNAIL_URL;

  private static List<Pair<String, String>> latestUpgrades;
  private static List<Pair<String, String>> latestFixes;
  private static Version latestVersion;
  private static Texture2D latestThumb;
  private static String latestTitle;

  private static XmlDocument latestPatch;

  public static boolean suppress;
  public static boolean userInitiated;

  public UpdateRequest(boolean isManual/* = false*/) {}

  public static List<Pair<String, String>> getLatestUpgrades() { return latestUpgrades; }
  public static List<Pair<String, String>> getLatestFixes() { return latestFixes; }
  public static Version getLatestVersion() { return latestVersion; }
  public static Texture2D getLatestThumb() { return latestThumb; }
  public static String getLatestTitle() { return latestTitle; }

  public static boolean isDone() {}

  public static boolean updatesAreAvailable() {}

  @Override
  public CompletableFuture<Boolean> downloadAsync() {}

  private boolean handleResponse(String latestXml) {}
}
