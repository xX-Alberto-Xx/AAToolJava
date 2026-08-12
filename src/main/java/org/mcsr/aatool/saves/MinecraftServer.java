package org.mcsr.aatool.saves;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Time;
import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.configuration.SftpConfig;
import org.mcsr.aatool.enums.SyncState;
import org.mcsr.aatool.net.Server;
import org.mcsr.aatool.utilities.Strings;
import org.mcsr.aatool.utilities.Timer;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.Response.StatusCode;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.sftp.SFTPException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;

public final class MinecraftServer {
  private static final String SFTP_PREFIX = "sftp://";
  private static final double RETRY_INTERVAL = 10;
  private static final int MAXIMUM_RETRIES = 3;
  private static final long ATTEMPT_INTERVAL_MS = 5000;

  private static Exception lastError;
  private static Instant lastWorldSave;
  private static SyncState state;
  private static String messageOfTheDay;
  private static String worldName;

  private static final Timer REFRESH_TIMER = new Timer();

  private static boolean credentialsValidated;
  private static int currentDownloadPercent;
  private static float smoothDownloadPercent;

  private MinecraftServer() {}

  public static Exception getLastError() { return lastError; }
  public static Instant getLastWorldSave() { return lastWorldSave; }
  public static SyncState getState() { return state; }
  public static String getMessageOfTheDay() { return messageOfTheDay; }

  public static int getSaveInterval() { return Config.getSftp().autoSaveMinutes.getValue() * 60 + 5; }
  public static boolean isLastSyncFailed() { return lastError != null; }
  public static boolean isDownloading() { return state == SyncState.ADVANCEMENTS || state == SyncState.STATISTICS; }
  public static boolean isEnabled() { return Config.getTracking().useSftp.getValue(); }

  public static int getNextRefresh() { return (int) Math.max(Math.ceil(REFRESH_TIMER.timeLeft), 0); }
  public static void invalidateWorld() { worldName = ""; }
  private static void setState(SyncState state) { MinecraftServer.state = state; }

  public static Instant getRefreshEstimate() {
    return isEnabled() ? lastWorldSave.plusSeconds(getSaveInterval()) : Instant.MIN;
  }

  public static void update(Time time) {
    if (lastError instanceof IllegalArgumentException) {
      // Invalid login credentials, don't try reconnecting
      return;
    }

    REFRESH_TIMER.update(time);
    if (isEnabled() && REFRESH_TIMER.isExpired()) sync();

    // Smoothly interpolate progress percentage for status label
    float speed = (float) (8 * time.getDelta());
    smoothDownloadPercent = smoothDownloadPercent * (1 - speed) + currentDownloadPercent * speed;
  }

  public static void sync() {
    if (state != SyncState.READY) return;
    setState(SyncState.CONNECTING);

    // Attempt to sync in the background
    CompletableFuture.runAsync(() -> {
      SftpClient sftp = null;
      boolean success = false;

      try {
        sftp = tryConnect();
        if (sftp == null || !tryDownloadServerProperties(sftp)) return;

        Instant latest = tryGetWorldSaveTime(sftp);
        if (latest == null) return;

        double saveInterval = getSaveInterval();
        double remaining = saveInterval - latest.until(Instant.now(), ChronoUnit.NANOS) / 1_000_000_000d;
        if (remaining > 0) REFRESH_TIMER.setAndStart(Math.min(remaining, saveInterval));

        if (latest.equals(lastWorldSave) || !tryDownloadProgress(sftp)) return;

        lastWorldSave = latest;

        // Update client refresh estimates if hosting
        Server server = Server.tryGet();
        if (server != null) server.sendNextRefresh();

        success = true;
      } finally {
        if (sftp != null) {
          try { sftp.close(); }
          catch (IOException e) { throw new UncheckedIOException(e); }
        }

        if (REFRESH_TIMER.isExpired() || (
          isLastSyncFailed() && !(lastError instanceof IllegalArgumentException)
        )) {
          REFRESH_TIMER.setAndStart(RETRY_INTERVAL);
        }

        setState(SyncState.READY);

        if (success) {
          Tracker.fileSystemChanged();
          Tracker.invalidate();
        }
      }
    });
  }

  public static String getLongStatusText() {
    return state == SyncState.CONNECTING ? "Connecting to Minecraft server..."
         : credentialsValidated ? switch (state) {
           // Waiting
           case READY -> (
             lastError instanceof SocketException ? "Couldn't reach dedicated Minecraft server. Retrying in " :
             getNotFoundStatus(lastError) != null ? lastError.getMessage() + " Retrying in " :
             lastError instanceof IOException ? "SFTP couldn't write to local files! Retrying in " :
             isLastSyncFailed() ? "SFTP Error: " + lastError.getMessage() + " Retrying in " :
             "Synced! Refreshing in "
           ) + Tracker.getEstimateString(getNextRefresh());
           // Busy
           case CONNECTING -> "Connecting to Minecraft server...";
           case SERVER_PROPERTIES -> "Parsing server properties...";
           case LAST_AUTO_SAVE -> "Comparing world time-stamps...";
           case ADVANCEMENTS -> "Syncing advancements... " + Math.ceil(smoothDownloadPercent) + '%';
           case STATISTICS -> "Syncing statistics... " + Math.ceil(smoothDownloadPercent) + '%';
           default -> "Syncing...";
         }
         : lastError instanceof UserAuthException ? "SFTP login refused by Minecraft server."
         : lastError instanceof IllegalArgumentException ? "Invalid SFTP login."
         : "SFTP not running: Retrying in " + Tracker.getEstimateString(getNextRefresh());
  }

  public static String getShortStatusText() {
    return switch (state) {
      case READY -> credentialsValidated
        ? "Refreshing in " + Tracker.getEstimateString(getNextRefresh()).replace(' ', '\0')
        : "SFTP Offline";
      case CONNECTING -> "Connecting...";
      default -> "Syncing...";
    };
  }

  private static SftpClient tryConnect() {
    lastError = null;

    // Fix host formatting if needed
    SftpConfig sftpConfig = Config.getSftp();
    String host = sftpConfig.host.getValue();
    if (host.startsWith(SFTP_PREFIX)) host = host.substring(SFTP_PREFIX.length());

    try {
      // Start SFTP client and attempt connection
      SftpClient sftp = new SftpClient(
        host,
        sftpConfig.port.getValue(),
        sftpConfig.username.getValue(),
        sftpConfig.password.getValue(),
        5000
      );

      credentialsValidated = true;
      return sftp;
    } catch (IllegalArgumentException exception) {
      lastError = exception;
      credentialsValidated = false;
      return null;
    } catch (IOException exception) {
      // Couldn't connect to SFTP server
      REFRESH_TIMER.setAndStart(RETRY_INTERVAL);
      lastError = exception;
      credentialsValidated = !(exception instanceof UserAuthException);
      return null;
    }
  }

  private static boolean tryDownloadProgress(SftpClient sftp) {
    // Download advancements JSONs
    setState(SyncState.ADVANCEMENTS);
    if (!tryDownloadFolder(sftp, "advancements")) return false;

    // Download statistics JSONs
    setState(SyncState.STATISTICS);
    return tryDownloadFolder(sftp, "stats");
  }

  private static String tryGetProperty(String[] properties, String key) {
    // Parse value from server properties ini
    for (String property : properties) {
      if (property.startsWith(key)) {
        // Value is everything right of the '=' symbol
        int equalsIndex = property.indexOf('=');
        if (equalsIndex == -1) throw new IllegalArgumentException("Missing '=' symbol: " + property);
        return property.substring(equalsIndex + 1).stripTrailing();
      }
    }

    return null;
  }

  private static boolean tryDownloadServerProperties(SftpClient sftp) {
    return tryDownloadServerProperties(sftp, 0);
  }

  private static boolean tryDownloadServerProperties(SftpClient sftp, int failures) {
    if (!Strings.isNullOrEmpty(worldName)) {
      // Early exit if properties already downloaded (assume they haven't changed)
      return true;
    }

    setState(SyncState.SERVER_PROPERTIES);

    try {
      // Download server properties
      String[] properties = sftp.readAllText(
        Config.getSftp().serverRoot.getValue() + "/server.properties"
      ).split("\n");

      String world = tryGetProperty(properties, "level-name");
      if (world != null) worldName = world;

      String message = tryGetProperty(properties, "motd");
      if (message != null) messageOfTheDay = message;

      return true;
    } catch (IOException | IllegalArgumentException exception) {
      if (exception instanceof IOException && failures < MAXIMUM_RETRIES) {
        // Network error. Try downloading properties again
        try {
          Thread.sleep(ATTEMPT_INTERVAL_MS);
          return tryDownloadServerProperties(sftp, failures + 1);
        } catch (InterruptedException e) { exception.addSuppressed(e); }
      }

      // Fatal error or out of retries. Give up
      lastError = exception;
      return false;
    }
  }

  private static Instant tryGetWorldSaveTime(SftpClient sftp) { return tryGetWorldSaveTime(sftp, 0); }
  private static Instant tryGetWorldSaveTime(SftpClient sftp, int failures) {
    setState(SyncState.LAST_AUTO_SAVE);
    String remotePath = Config.getSftp().serverRoot.getValue() + '/' + worldName + "/level.dat";

    try {
      return sftp.getLastWriteTime(remotePath);
    } catch (IOException exception) {
      if (failures < MAXIMUM_RETRIES) {
        StatusCode notFoundStatus = getNotFoundStatus(exception);

        if (notFoundStatus != null) {
          // Folder not found, world name might be wrong. Refresh it next time
          lastError = new SFTPException(
            notFoundStatus,
            "File not found (note: SFTP uses / as separator regardless of the server OS): \"" + remotePath + "\"."
          );

          invalidateWorld();
          return null;
        }

        // Try getting last write time again
        try {
          Thread.sleep(ATTEMPT_INTERVAL_MS);
          return tryGetWorldSaveTime(sftp, failures + 1);
        } catch (InterruptedException e) { exception.addSuppressed(e); }
      }

      // Fatal error or out of retries. Give up
      lastError = exception;
      return null;
    }
  }

  private static boolean tryDownloadFolder(SftpClient sftp, String name) {
    return tryDownloadFolder(sftp, name, 0);
  }

  private static boolean tryDownloadFolder(SftpClient sftp, String name, int failures) {
    // Reset progress counter
    currentDownloadPercent = 0;
    smoothDownloadPercent = 0;

    Path localPath = Paths.System.SFTP_WORLDS_FOLDER.resolve(worldName).resolve(name);
    String remotePath = Config.getSftp().serverRoot.getValue() + '/' + worldName + '/' + name;

    try {
      // Make sure directory exists
      Files.createDirectories(localPath);

      // Get new and old file lists
      List<Path> localFiles = getFiles(localPath);
      List<RemoteResourceInfo> remoteFiles = sftp.listDirectory(remotePath);

      // Sync folder
      deleteDeprecatedFiles(remoteFiles, localFiles);
      downloadAll(sftp, remoteFiles, localPath);
      return true;
    } catch (IOException | InterruptedException exception) {
      if (exception instanceof IOException && failures < MAXIMUM_RETRIES) {
        StatusCode notFoundStatus = getNotFoundStatus(exception);

        if (notFoundStatus != null) {
          // Folder not found, so world name might be wrong. Refresh it next time
          lastError = new SFTPException(notFoundStatus, "Path not found: \"" + remotePath + "\".");
          invalidateWorld();
          return false;
        }

        // Try downloading the folder again
        try {
          Thread.sleep(ATTEMPT_INTERVAL_MS);
          return tryDownloadFolder(sftp, name, failures + 1);
        } catch (InterruptedException e) { exception.addSuppressed(e); }
      }

      // Fatal error or out of retries. Give up
      lastError = exception;
      return false;
    }
  }

  private static boolean tryDownloadFile(SftpClient sftp, String remote, Path local) throws InterruptedException {
    return tryDownloadFile(sftp, remote, local, 0);
  }

  private static boolean tryDownloadFile(
    SftpClient sftp, String remote, Path local, int failures
  ) throws InterruptedException {
    try {
      sftp.downloadFile(remote, local);
      return true;
    } catch (IOException exception) {
      if (failures < MAXIMUM_RETRIES) {
        // Try downloading the file again
        Thread.sleep(ATTEMPT_INTERVAL_MS);
        return tryDownloadFile(sftp, remote, local, failures + 1);
      }

      // Fatal error or out of retries. Give up
      lastError = exception;
      return false;
    }
  }

  private static void deleteDeprecatedFiles(List<RemoteResourceInfo> source, List<Path> destination) {
    // Remove deprecated files
    localFiles:
    for (Path localFile : destination) {
      String localName = localFile.getFileName().toString();

      for (RemoteResourceInfo remoteFile : source) {
        if (remoteFile.getName().equals(localName)) {
          // File is still supposed to exist
          continue localFiles;
        }
      }

      try {
        // File no longer on server (probably a run reset). Clean it up
        Files.delete(localFile);
      } catch (IOException ignored) {
        // Couldn't delete old file (probably open externally for some reason)
        // Just leave it be and move on
      }
    }
  }

  private static void downloadAll(
    SftpClient sftp, List<RemoteResourceInfo> files, Path downloadFolder
  ) throws InterruptedException {
    // Download remote files from server over SFTP
    int counter = 1;

    for (RemoteResourceInfo remoteFile : files) {
      // Update percentage
      currentDownloadPercent = counter * 100 / files.size();
      counter++;
      if (!remoteFile.isRegularFile()) continue;

      // Download to local file
      tryDownloadFile(sftp, remoteFile.getPath(), downloadFolder.resolve(remoteFile.getName()));
    }
  }

  private static List<Path> getFiles(Path directory) throws IOException {
    // Iterate top level files
    try (Stream<Path> stream = Files.find(directory, 1, (p, attrs) -> !attrs.isDirectory())) {
      return stream.toList();
    }
  }

  private static StatusCode getNotFoundStatus(Exception exception) {
    if (!(exception instanceof SFTPException sftpException)) return null;

    StatusCode status = sftpException.getStatusCode();
    return status == StatusCode.NO_SUCH_FILE || status == StatusCode.NO_SUCH_PATH ? status : null;
  }

  private static class SftpClient implements AutoCloseable {
    private final SSHClient ssh = new SSHClient();
    private final SFTPClient sftp;

    private SftpClient(
      String host, int port, String username, String password, int connectTimeout
    ) throws IOException {
      try {
        if (username.isBlank()) throw new IllegalArgumentException("Invalid username");

        this.ssh.addHostKeyVerifier(new PromiscuousVerifier());
        this.ssh.setConnectTimeout(connectTimeout);
        this.ssh.connect(host, port);
        this.ssh.authPassword(username, password);
        this.sftp = this.ssh.newSFTPClient();
      } catch (IllegalArgumentException | IOException exception) {
        try { this.ssh.close(); }
        catch (IOException closeException) { exception.addSuppressed(closeException); }
        throw exception;
      }
    }

    public String readAllText(String path) throws IOException {
      try (RemoteFile file = this.sftp.open(path)) {
        return new String(file.new RemoteFileInputStream().readAllBytes(), StandardCharsets.UTF_8);
      }
    }

    public Instant getLastWriteTime(String path) throws IOException {
      return Instant.ofEpochSecond(this.sftp.mtime(path));
    }

    public List<RemoteResourceInfo> listDirectory(String path) throws IOException {
      return this.sftp.ls(path);
    }

    public void downloadFile(String remote, Path local) throws IOException {
      try (RemoteFile file = this.sftp.open(remote)) {
        Files.copy(file.new RemoteFileInputStream(), local, StandardCopyOption.REPLACE_EXISTING);
      }
    }

    @Override
    public void close() throws IOException {
      try (this.ssh) { this.sftp.close(); }
    }
  }
}
