package org.mcsr.aatool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.configuration.TrackingConfig;
import org.mcsr.aatool.data.categories.*;
import org.mcsr.aatool.data.objectives.*;
import org.mcsr.aatool.data.progress.NetworkState;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.data.progress.WorldState;
import org.mcsr.aatool.enums.ProgressFilter;
import org.mcsr.aatool.enums.TrackerSource;
import org.mcsr.aatool.exceptions.InvalidPathException;
import org.mcsr.aatool.exceptions.NoSavesFolderException;
import org.mcsr.aatool.exceptions.NoWorldException;
import org.mcsr.aatool.net.Client;
import org.mcsr.aatool.net.Lobby;
import org.mcsr.aatool.net.Peer;
import org.mcsr.aatool.net.Player;
import org.mcsr.aatool.net.Protocol;
import org.mcsr.aatool.net.Server;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.saves.MinecraftServer;
import org.mcsr.aatool.saves.WorldFolder;
import org.mcsr.aatool.utilities.ActiveInstance;
import org.mcsr.aatool.utilities.FileSystemWatcher;
import org.mcsr.aatool.utilities.Pair;
import org.mcsr.aatool.utilities.Result;
import org.mcsr.aatool.utilities.Strings;
import org.mcsr.aatool.utilities.Timer;

public final class Tracker {
  public static final AdvancementManifest ADVANCEMENTS = new AdvancementManifest();
  public static final AchievementManifest ACHIEVEMENTS = new AchievementManifest();
  public static final ComplexObjectiveManifest COMPLEX_OBJECTIVES = new ComplexObjectiveManifest();
  public static final BlockManifest BLOCKS = new BlockManifest();
  public static final DeathManifest DEATHS = new DeathManifest();

  private static final BiPredicate<Path, BasicFileAttributes> IS_DIRECTORY = (p, attrs) -> attrs.isDirectory();

  private static Category category;
  private static WorldState state = new WorldState();
  private static Exception lastError;
  private static boolean designationsChanged;
  private static boolean mainPlayerChanged;
  private static boolean coOpStateChanged;
  private static boolean worldLocked;
  private static String status;

  public static boolean manualChecklistInvalidated;
  public static boolean manualChecklistChanged;

  private static final FileSystemWatcher WORLD_WATCHER = new FileSystemWatcher();
  private static final FileSystemWatcher PRACTICE_WORLD_WATCHER = new FileSystemWatcher();
  private static final FileSystemWatcher ADVANCEMENTS_WATCHER = new FileSystemWatcher();
  private static final FileSystemWatcher STATISTICS_WATCHER = new FileSystemWatcher();

  private static WorldFolder world;
  private static Timer refreshTimer;
  private static Timer filesystemEventDebounce;
  private static Duration lastInGameTime = Duration.ZERO;
  private static Uuid previousMainPlayer = Uuid.EMPTY;
  private static Path previousSavesPath;
  private static Path previousWorldPath;
  private static String lastServerMessage;
  private static int previousActiveId = -1;
  private static boolean fileSystemEventRaised;
  private static double lastRefresh;

  private Tracker() {}

  public static Category getCategory() { return category; }
  public static WorldState getState() { return state; }
  public static Exception getLastError() { return lastError; }
  public static boolean areDesignationsChanged() { return designationsChanged; }
  public static boolean isMainPlayerChanged() { return mainPlayerChanged; }
  public static boolean isCoOpStateChanged() { return coOpStateChanged; }
  public static boolean isWorldLocked() { return worldLocked; }
  public static String getStatus() { return status; }

  public static boolean isInvalidated() {
    return isSavesFolderChanged() || areObjectivesChanged() || isProgressChanged();
  }

  public static boolean isSavesFolderChanged() {
    return !Config.getTracking().manualChecklistMode.getValue()
        && !Paths.Saves.currentFolder().equals(previousSavesPath);
  }

  public static boolean isWorldChanged() { return world.isPathChanged(); }

  public static boolean areObjectivesChanged() {
    TrackingConfig trackingConfig = Config.getTracking();
    return trackingConfig.gameCategory.isChanged() || trackingConfig.gameVersion.isChanged();
  }

  public static boolean isProgressChanged() {
    return world.isProgressChanged() || world.isPathChanged() || world.isInvalidated() || coOpStateChanged;
  }

  public static boolean isWorking() { return lastError == null; }
  public static String getCurrentCategory() { return category != null ? category.getName() : null; }
  public static String getCurrentVersion() { return category != null ? category.getCurrentVersion() : null; }

  public static AdvancementManifest getCurrentAdvancementSet() {
    return category instanceof AllAchievements ? ACHIEVEMENTS : ADVANCEMENTS;
  }

  public static Map<Pair<String, String>, Criterion> getAllCriteria() {
    return getCurrentAdvancementSet().allCriteria;
  }

  public static Map<Pair<String, String>, Criterion> getRemainingCriteria() {
    return getCurrentAdvancementSet().remainingCriteria;
  }

  public static String getWorldName() { return world.getName(); }
  public static Duration getInGameTime() { return state.inGameTime; }
  public static boolean isInGameTimeChanged() { return !state.inGameTime.equals(lastInGameTime); }
  public static TrackerSource getSource() { return Config.getTracking().source.getValue(); }

  public static void toggleWorldLock() { worldLocked ^= true; }

  public static String getFullIgt() {
    Duration igt = getInGameTime();
    return igt.toHours() + ":" + pad2(igt.toMinutesPart()) + ':' + pad2(igt.toSecondsPart());
  }

  public static String getShortIgt() {
    Duration igt = getInGameTime();
    return pad2(igt.toHoursPart()) + ':' + pad2(igt.toMinutesPart()) + ':' + pad2(igt.toSecondsPart());
  }

  public static String getDays() {
    Duration igt = getInGameTime();
    if (igt.toDays() == 0) return "";

    long millis = igt.toMillis();
    // Assuming millis >= 0, round millis / 1000 / 60 / 60 / 24 * 100 to the nearest integer,
    // picking the even one in case of a tie
    long dayCents = (millis + 432_000) / 1_728_000 + (millis + 1_295_999) / 1_728_000;
    return dayCents / 100 + "." + pad2((int) (dayCents % 100)) + " IRL Days";
  }

  public static String getDaysAndHours() {
    Duration igt = getInGameTime();
    long days = igt.toDays();

    return days == 0 ? ""
         : (days == 1 ? "1 Day, " : days + " Days, ")
           + igt.toHoursPart() + '.' + pad2(igt.toMinutesPart() * 5 / 3) + " Hrs Played";
  }

  private static String pad2(int n) { return n < 10 ? "0" + n : Integer.toString(n); }

  public static String getEstimateString(long seconds) {
    return seconds >= 60
           ? seconds / 60 + " min & " + seconds % 60 + " sec"
           : seconds + " seconds";
  }

  public static String getLastRefresh(Time time) {
    int seconds = (int) Math.max(time.getTotalSeconds() - lastRefresh, 0);

    return seconds < 1 ? "Refreshing Now"
         : seconds == 1 ? "Refreshed 1 second ago"
         : "Refreshed " + getEstimateString(seconds).replace("& ", "") + " ago";
  }

  public static void invalidateDesignations() {
    designationsChanged = true;
    getCurrentAdvancementSet().refreshRemainingCriteria();
  }

  public static Result<Advancement> tryGetAdvancement(String id) {
    return getCurrentAdvancementSet().tryGetAdvancement(id);
  }

  public static Result<Criterion> tryGetCriterion(String adv, String crit) {
    Map<Pair<String, String>, Criterion> allCriteria = getAllCriteria();
    Pair<String, String> key = new Pair<>(adv, crit);
    return new Result<>(allCriteria.containsKey(key), allCriteria.get(key));
  }

  public static Result<Set<Advancement>> tryGetAdvancementGroup(String id) {
    return ADVANCEMENTS.tryGetGroup(id);
  }

  public static Result<ComplexObjective> tryGetComplexObjective(String typeName) {
    return COMPLEX_OBJECTIVES.tryGet(typeName);
  }

  public static Result<Block> tryGetBlock(String id) { return BLOCKS.tryGet(id); }

  public static Result<Death> tryGetDeath(String id) { return DEATHS.tryGet(id); }

  public static Uuid getMainPlayer() {
    TrackingConfig trackingConfig = Config.getTracking();
    Uuid mainPlayer;

    if (trackingConfig.filter.getValue() == ProgressFilter.SOLO) {
      mainPlayer = Player.tryGetUuid(trackingConfig.soloFilterName.getValue());
      if (mainPlayer == null) mainPlayer = Uuid.EMPTY;
    } else {
      Iterator<Uuid> players = state.players.keySet().iterator();
      mainPlayer = players.hasNext() ? players.next() : Uuid.EMPTY;
    }

    if (mainPlayer.equals(Uuid.EMPTY)) {
      mainPlayer = trackingConfig.lastUuid.getValue();

      if (mainPlayer.equals(Uuid.EMPTY)) {
        mainPlayer = Player.tryGetUuid(trackingConfig.lastPlayer.getValue());
        if (mainPlayer == null) mainPlayer = Uuid.EMPTY;
      }
    }

    mainPlayerChanged |= !mainPlayer.equals(previousMainPlayer);
    trackingConfig.lastUuid.set(mainPlayer);
    previousMainPlayer = mainPlayer;
    return mainPlayer;
  }

  public static Set<Uuid> getAllPlayers() {
    Set<Uuid> ids = new HashSet<>(state.players.keySet());

    if (Peer.isConnected()) {
      Lobby lobby = Peer.tryGetLobby();

      if (lobby != null) {
        ids.addAll(lobby.users.keySet());
        ids.remove(Uuid.EMPTY);
        return ids;
      }
    }

    TrackingConfig trackingConfig = Config.getTracking();

    if (trackingConfig.filter.getValue() == ProgressFilter.SOLO) {
      Uuid soloPlayer = Player.tryGetUuid(trackingConfig.soloFilterName.getValue());
      if (soloPlayer != null) ids.add(soloPlayer);
    }

    ids.remove(Uuid.EMPTY);
    return ids;
  }

  public static void initialize() {
    world = new WorldFolder();
    state = new WorldState();
    refreshTimer = new Timer();

    filesystemEventDebounce = new Timer(0.5);
    filesystemEventDebounce.expire();

    mainPlayerChanged = true;

    TrackingConfig trackingConfig = Config.getTracking();
    String lastVersion = trackingConfig.gameVersion.getValue();
    trySetCategory(trackingConfig.gameCategory.getValue());
    trySetVersion(lastVersion);
  }

  public static void fileSystemChanged() { fileSystemEventRaised = true; }

  public static String getStatusText() {
    return Peer.isServer() && Peer.isConnected() ? "Hosting: \"" + getWorldName() + '"'
         : Config.getTracking().manualChecklistMode.getValue() ? "Manual Mode: Click items to mark as complete"
         : isWorking() ?
           getSource() == TrackerSource.ACTIVE_INSTANCE && ActiveInstance.hasNumber()
           ? "Instance " + ActiveInstance.getNumber() + ": \"" + getWorldName() + '"'
           : "Tracking: \"" + getWorldName() + '"'
         : lastError.getMessage();
  }

  public static boolean trySetCategory(String category) {
    if (Strings.isNullOrEmpty(category)) return false;

    // Check if category is the same
    if (Tracker.category != null && category.equals(Tracker.category.getName())) return false;

    try {
      Tracker.category = switch (category.toLowerCase().replace(" ", "").replace("_", "")) {
        // Main categories
        case "alladvancements" -> new AllAdvancements();
        case "allachievements" -> new AllAchievements();
        case "halfpercent"     -> new HalfPercent();

        // Single advancement categories
        case "balanceddiet"    -> new BalancedDiet();
        case "adventuringtime" -> new AdventuringTime();
        case "monstershunted"  -> new MonstersHunted();

        // Random extensions
        case "allblocks" -> new AllBlocks();
        case "alldeaths" -> new AllDeaths();
        case "halfdeaths" -> new HalfDeaths();
        case "allportals" -> new AllPortals();
        case "allsmithingtemplates" -> new AllSmithingTemplates();

        default -> throw new IllegalArgumentException("Category not supported: \"" + category + "\".");
      };

      // Save change to config
      TrackingConfig trackingConfig = Config.getTracking();
      trackingConfig.gameCategory.set(Tracker.category.getName());
      trackingConfig.gameVersion.set(Tracker.category.getCurrentVersion());
      trackingConfig.trySave();
      Tracker.category.loadObjectives();
      return true;
    } catch (IllegalArgumentException ignored) {
      if (Tracker.category == null) {
        // Fallback to All Advancements
        Tracker.category = new AllAdvancements();
        TrackingConfig trackingConfig = Config.getTracking();
        trackingConfig.gameCategory.set(Tracker.category.getName());
        trackingConfig.trySave();
        Tracker.category.loadObjectives();
      }

      return false;
    }
  }

  public static boolean trySetVersion(String versionNumber) { return category.trySetVersion(versionNumber); }

  public static void invalidate() { invalidate(false); }
  public static void invalidate(boolean invalidateWorld) {
    if (invalidateWorld) world.invalidate();
    refreshTimer.expire();
  }

  public static void clearFlags() {
    lastInGameTime = getInGameTime();
    designationsChanged = false;
    mainPlayerChanged = false;
    coOpStateChanged = false;
    manualChecklistChanged = false;
    world.clearFlags();
  }

  public static void update(Time time) {
    refreshTimer.update(time);
    Client client = Client.tryGet();

    if (client != null) {
      parseCoOpProgress(time, client);
    } else {
      TrackingConfig trackingConfig = Config.getTracking();

      if (trackingConfig.manualChecklistMode.getValue() && category instanceof AllAdvancements) {
        if (
          fileSystemEventRaised ||
          manualChecklistInvalidated ||
          areObjectivesChanged() ||
          trackingConfig.isSourceChanged()
        ) {
          readManualChecklist(time);
          manualChecklistChanged = true;
          manualChecklistInvalidated = false;
        }
      } else {
        if (
          (fileSystemEventRaised && filesystemEventDebounce.isExpired()) ||
          areObjectivesChanged() ||
          trackingConfig.isSourceChanged() ||
          (ActiveInstance.isWatching() && previousActiveId != ActiveInstance.getLastActiveId())
        ) {
          updateCurrentWorld();
          readLocalFiles(time);
          filesystemEventDebounce.reset();
        }

        previousActiveId = ActiveInstance.getLastActiveId();
        updateFileSystemWatchers();
      }
    }

    category.update();
    COMPLEX_OBJECTIVES.updateDynamicIcons(time);
    filesystemEventDebounce.update(time);
  }

  private static void updateFileSystemWatchers() {
    Path fullName = world.getFullName();

    if (!Paths.isNullOrEmpty(fullName)) {
      Path advancements = fullName.resolve("advancements");

      if (!ADVANCEMENTS_WATCHER.isRaisingEvents() && Files.isDirectory(advancements)) {
        ADVANCEMENTS_WATCHER.setPath(advancements);
        ADVANCEMENTS_WATCHER.enableRaisingEvents();
      }

      Path statistics = fullName.resolve("stats");

      if (!STATISTICS_WATCHER.isRaisingEvents() && Files.isDirectory(statistics)) {
        STATISTICS_WATCHER.setPath(statistics);
        STATISTICS_WATCHER.enableRaisingEvents();
      }
    }

    fileSystemEventRaised = false;
  }

  private static void updateCurrentWorld() {
    TrackingConfig trackingConfig = Config.getTracking();
    if (trackingConfig.source.isChanged()) worldLocked = false;

    Path savesPath = Paths.EMPTY_PATH;
    Path worldPath = null;

    try {
      Path practiceSavesPath = null;
      boolean practiceFolderExists = false;
      boolean isPracticeWorld = false;

      if (getSource() == TrackerSource.SPECIFIC_WORLD && !trackingConfig.useSftp.getValue()) {
        // Set world to user-defined path
        worldLocked = true;
        worldPath = trackingConfig.customWorldPath.getValue();

        // Check if path is empty
        if (Paths.isNullOrEmpty(worldPath)) {
          if (!(lastError instanceof IllegalArgumentException) || trackingConfig.isSourceChanged()) {
            throw new IllegalArgumentException("User-specified world path empty");
          }

          return;
        }
      } else {
        // Get current saves folder
        savesPath = Paths.Saves.currentFolder();
        practiceSavesPath = Paths.Saves.currentPracticeSavesFolder();

        // Exit early if path is invalid
        if (lastError instanceof InvalidPathException && savesPath.equals(previousSavesPath)) return;

        // Unlock world if saves folder changed
        if (!savesPath.equals(previousSavesPath)) worldLocked = false;

        // Make sure path isn't empty
        if (Paths.isNullOrEmpty(savesPath)) {
          if (!(lastError instanceof IllegalArgumentException) || trackingConfig.isSourceChanged()) {
            throw new IllegalArgumentException(
              getSource() == TrackerSource.ACTIVE_INSTANCE
              ? "Tab into Minecraft to start tracking"
              : "Custom saves path is empty"
            );
          }

          return;
        }

        FileTime latestTime = null;

        try {
          if (worldLocked) {
            // Make sure folder actually exists
            if (!Files.isDirectory(savesPath)) throw new NoSuchFileException(savesPath.toString());

            // Keep same world
            worldPath = previousWorldPath;
          } else {
            // Find most recently modified world in folder
            try (Stream<Path> potentialWorlds = Files.find(savesPath, 1, IS_DIRECTORY)) {
              Iterator<Path> foldersIterator = potentialWorlds.iterator();
              foldersIterator.next(); // Skip savesPath itself

              while (foldersIterator.hasNext()) {
                Path worldFolder = foldersIterator.next();

                // Skip any folders that definitely aren't worlds
                if (!Paths.Saves.mightBeWorldFolder(worldFolder)) continue;

                // Sort by write time
                FileTime time = Files.getLastModifiedTime(worldFolder);

                if (latestTime == null || time.compareTo(latestTime) > 0) {
                  worldPath = worldFolder;
                  latestTime = time;
                }
              }
            }
          }
        } catch (NoSuchFileException ignored) {
          // Avoid re-throwing duplicate exception
          if (!(lastError instanceof NoSavesFolderException)) {
            throw new NoSavesFolderException(savesPath.toAbsolutePath().toString());
          }

          return;
        }

        if (!worldLocked) {
          // Include practice saves if present
          Path[] potentialPracticeWorlds = {};

          try (Stream<Path> potentialPracticeFolders = Files.find(practiceSavesPath, 1, IS_DIRECTORY)) {
            // Skip practiceSavesPath itself
            potentialPracticeWorlds = potentialPracticeFolders.skip(1).toArray(Path[]::new);
            practiceFolderExists = true;
          } catch (Exception e) {
            practiceFolderExists = !(e instanceof NoSuchFileException);
          }

          for (Path worldFolder : potentialPracticeWorlds) {
            // Skip any folders that definitely aren't worlds
            if (!Paths.Saves.mightBeWorldFolder(worldFolder)) continue;

            // Sort by write time
            FileTime time = Files.getLastModifiedTime(worldFolder);

            if (latestTime == null || time.compareTo(latestTime) > 0) {
              worldPath = worldFolder;
              latestTime = time;
              isPracticeWorld = true;
            }
          }
        }
      }

      // Make sure folder actually exists
      if (worldPath == null || !Files.isDirectory(worldPath)) {
        if (lastError instanceof NoWorldException && (
          worldPath == null
          ? world == null
          : world != null && worldPath.toAbsolutePath().equals(world.getFullName())
        )) {
          return;
        }

        throw new NoWorldException();
      }

      if (!worldPath.toAbsolutePath().equals(world.getFullName())) {
        world.setPath(worldPath);
        Path worldsFolder = worldPath.toAbsolutePath().getParent();

        if (practiceFolderExists && isPracticeWorld) {
          PRACTICE_WORLD_WATCHER.setPath(practiceSavesPath);
          PRACTICE_WORLD_WATCHER.enableRaisingEvents();
          WORLD_WATCHER.setPath(worldsFolder.resolveSibling("saves"));
        } else {
          WORLD_WATCHER.setPath(worldsFolder);
        }

        WORLD_WATCHER.enableRaisingEvents();
        ADVANCEMENTS_WATCHER.disableRaisingEvents();
        STATISTICS_WATCHER.disableRaisingEvents();
      }

      lastError = null;
    } catch (IOException | IllegalArgumentException e) {
      if (!world.isEmpty()) {
        world.unset();
        worldLocked = false;
      }

      lastError = e;
    } finally {
      previousSavesPath = savesPath;
      previousWorldPath = worldPath;
    }
  }

  private static void parseCoOpProgress(Time time, Client client) {
    // Update world from co-op server
    if (client == null) return;

    Result<String> progress = client.tryGetData(Protocol.Headers.PROGRESS);
    if (!progress.success || progress.value.equals(lastServerMessage)) return;

    coOpStateChanged = true;
    lastServerMessage = progress.value;
    NetworkState networkState = NetworkState.fromJsonString(progress.value);
    state = new WorldState(networkState);

    // Sync category and version with host
    trySetCategory(networkState.gameCategory);
    trySetVersion(networkState.gameVersion);

    // Reload objectives if game version has changed
    if (areObjectivesChanged()) category.loadObjectives();

    setState(state);
    lastRefresh = time.getTotalSeconds();
  }

  private static void readLocalFiles(Time time) {
    // Reload objective manifests if game version has changed
    if (areObjectivesChanged()) category.loadObjectives();

    // Wait to refresh until SFTP transfer is complete
    TrackingConfig trackingConfig = Config.getTracking();
    if (trackingConfig.useSftp.getValue() && MinecraftServer.isDownloading()) return;

    readLatestLog();

    // Update progress if source has been invalidated
    if (
      world.tryRefresh() ||
      Peer.isStateChanged() ||
      trackingConfig.isFilterChanged() ||
      trackingConfig.manualChecklistMode.isChanged()
    ) {
      lastServerMessage = null;
      state = world.getState();
      setState(state);

      // Broadcast changes to connected clients if server is running
      Server server = Server.tryGet();
      if (server != null && server.connected()) server.sendProgress();

      lastRefresh = time.getTotalSeconds();
    }
  }

  private static void readManualChecklist(Time time) {
    // Reload objective manifests if game version has changed
    if (areObjectivesChanged()) category.loadObjectives();

    // Update progress if source has been invalidated
    // TODO: ManualChecklistController
    setState(state);
  }

  private static void readLatestLog() {
    if (!(category instanceof AllDeaths)) return;

    // Attempt to sync death messages
    int before = state.deathMessages.size();
    state.syncDeathMessages();
    if (state.deathMessages.size() != before) DEATHS.updateState(state);
  }

  private static void setState(WorldState world) {
    ProgressState activeState;
    TrackingConfig trackingConfig = Config.getTracking();

    if (trackingConfig.filter.getValue() == ProgressFilter.COMBINED || Peer.isRunning()) {
      activeState = world;
    } else {
      Uuid player = Player.tryGetUuid(trackingConfig.soloFilterName.getValue());
      activeState = world.players.get(player != null ? player : Uuid.EMPTY);
    }

    ADVANCEMENTS.updateState(activeState);
    ACHIEVEMENTS.updateState(activeState);
    BLOCKS.updateState(activeState);
    COMPLEX_OBJECTIVES.updateState(activeState);
  }
}
