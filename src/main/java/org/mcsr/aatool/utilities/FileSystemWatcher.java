package org.mcsr.aatool.utilities;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.Tracker;

public class FileSystemWatcher {
  private static WatchService watcher = null;
  private static final PollThread pollThread = new PollThread();
  private static final Map<WatchKey, FileSystemWatcher> registered = new HashMap<>();

  private Path dir = null;
  private boolean raisingEvents = false;
  private WatchKey key = null;

  public boolean isRaisingEvents() { return this.raisingEvents; }

  public void setPath(Path dir) {
    if (Paths.isNullOrEmpty(dir)) {
      this.clear();
      return;
    }

    this.dir = dir;

    if (this.raisingEvents) {
      this.unregister();
      this.tryRegister();
    }
  }

  public void enableRaisingEvents() {
    this.raisingEvents = true;
    if (this.key == null && this.dir != null) this.tryRegister();
  }

  public void disableRaisingEvents() {
    this.unregister();
    this.raisingEvents = false;
  }

  private void tryRegister() {
    try {
      if (watcher == null) {
        watcher = FileSystems.getDefault().newWatchService();
        pollThread.start();
      }

      this.key = this.dir.register(
        watcher,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY
      );

      registered.put(this.key, this);
    } catch (IOException ignored) {}
  }

  private void clear() {
    this.dir = null;
    this.unregister();
  }

  private void unregister() {
    if (this.key != null) {
      registered.remove(this.key);
      this.key.cancel();
      this.key = null;
    }
  }

  private static class PollThread extends Thread {
    private PollThread() { this.setDaemon(true); }

    @Override
    public void run() {
      while (true) {
        WatchKey key;

        try {
          key = watcher.take();
        } catch (InterruptedException e) {
          for (Iterator<FileSystemWatcher> it = registered.values().iterator(); it.hasNext();) {
            FileSystemWatcher instance = it.next();
            it.remove();
            instance.clear();
          }

          try {
            watcher.close();
          } catch (IOException closeException) {
            closeException.addSuppressed(e);
            throw new UncheckedIOException(closeException);
          }

          break;
        }

        FileSystemWatcher instance = registered.get(key);
        if (instance == null) continue;

        if (!key.pollEvents().isEmpty()) Tracker.fileSystemChanged();
        if (!key.reset()) instance.clear();
      }
    }
  }
}
