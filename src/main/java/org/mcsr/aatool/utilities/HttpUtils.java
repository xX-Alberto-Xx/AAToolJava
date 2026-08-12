package org.mcsr.aatool.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.mcsr.aatool.net.Protocol;

public final class HttpUtils {
  private static final HttpClient CLIENT = HttpClient.newHttpClient();
  private static final HttpRequest.Builder PARTIAL_REQUEST = HttpRequest.newBuilder()
    .timeout(Duration.ofMillis(Protocol.Requests.TIMEOUT_NORMAL_MS))
    .GET();
  private static final HttpResponse.BodyHandler<String> STRING_HANDLER =
    HttpResponse.BodyHandlers.ofString();
  private static final HttpResponse.BodyHandler<InputStream> STREAM_HANDLER =
    HttpResponse.BodyHandlers.ofInputStream();

  private HttpUtils() {}

  public static CompletableFuture<String> getStringAsync(String urlString) {
    return getAsync(urlString, STRING_HANDLER);
  }

  public static CompletableFuture<InputStream> getStreamAsync(String urlString) {
    return getAsync(urlString, STREAM_HANDLER);
  }

  private static <T> CompletableFuture<T> getAsync(String urlString, HttpResponse.BodyHandler<T> handler) {
    URI url;
    try { url = new URI(urlString); }
    catch (URISyntaxException e) { return CompletableFuture.failedFuture(e); }

    return CLIENT.sendAsync(PARTIAL_REQUEST.uri(url).build(), handler).thenCompose(response -> {
      int statusCode = response.statusCode();

      return statusCode == 200
             ? CompletableFuture.completedStage(response.body())
             : CompletableFuture.failedStage(new IOException("Status code " + statusCode));
    });
  }
}
