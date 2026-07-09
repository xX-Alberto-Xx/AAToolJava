package org.mcsr.aatool.data;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mcsr.aatool.Paths;
import org.mcsr.aatool.net.Uuid;
import org.mcsr.aatool.utilities.Strings;

public final class Credits {
  public static final String DEVELOPER = "developer";
  public static final String DEDICATION = "special dedication";
  public static final String BETA_TESTER = "beta testers";

  public static final String GOLD_TIER = "supporter_gold";
  public static final String DIAMOND_TIER = "supporter_diamond";
  public static final String NETHERITE_TIER = "supporter_netherite";

  // Creator of AATool
  public static final String CTM = "60bddec7-939c-4753-a898-cffa33134a4d";
  public static final String CTM_NAME = "_ctm";

  // Completed the first ever Half-Heart Hardcore All Advancements speedrun
  public static final String ELYSAKU = "b2fcb273-9886-4a9b-bd7f-e005816fb7b7";
  public static final String ELYSAKU_NAME = "elysaku";

  // Completed 1000 Any% RSG speedruns in a row without resetting
  public static final String COURIWAY = "994f9376-3f80-48bc-9e72-ee92f861911d";
  public static final String COURIWAY_NAME = "couriway";

  // Completed 999 Any% RSG speedruns in a row without resetting FeelsStrongMan
  public static final String MOLEYG = "fa1bec35-0585-46c9-8f92-79f8be7cf9bc";
  public static final String MOLEYG_NAME = "moleyg";

  // Manages the AA community leaderboards
  public static final String DEADPOOL = "899c63ac-6590-46c0-b77c-4dae1543f707";
  public static final String DEADPOOL_NAME = "marvelord";

  // The best minecraft songs ever FeelsStrongMan
  public static final String CAPTAIN_SPARKLEZ = "5f820c39-5883-4392-b174-3125ac05e38c";
  public static final String CAPTAIN_SPARKLEZ_NAME = "captainsparklez";

  // The founding father of All Advancements
  public static final String ILLUMINA = "46405168-e9ce-40a0-99a4-0b989a912c77";
  public static final String ILLUMINA_NAME = "illumina";

  // First to complete 100 Hardcore runs in a row without dying
  public static final String FEINBERG = "9a8e24df-4c85-49d6-96a6-951da84fa5c4";
  public static final String FEINBERG_NAME = "feinberg";

  public static final Set<Credit> SPECIAL = Set.of(
    new Credit(DEVELOPER, "", "CTM", new Uuid("60bddec7-939c-4753-a898-cffa33134a4d"), "https://www.patreon.com/_ctm"),
    new Credit(DEDICATION, "", "Wroxy"),
    new Credit(BETA_TESTER, "", "Elysaku", new Uuid("b2fcb273-9886-4a9b-bd7f-e005816fb7b7"), "https://www.twitch.tv/elysaku"),
    new Credit(BETA_TESTER, "", "Churro :3", "https://www.instagram.com/theelysaku/")
  );

  private static boolean supporterSheetLoaded;

  private static boolean initialized = false;

  private static Set<Credit> all = Set.of();

  private static Map<String, Credit> byName = new HashMap<>();
  private static Map<Uuid, Credit> byUuid = new HashMap<>();

  private Credits() {}

  public static Set<Credit> getAll() { return all; }

  private static void ensureLookupsInitialized() {
    if (initialized) return;

    for (Credit credit : all) {
      byName.put(credit.name.toLowerCase(), credit);
      for (String alt : credit.altNames) byName.put(alt, credit);
      for (Uuid uuid : credit.uuids) byUuid.put(uuid, credit);
    }

    initialized = true;
  }

  public static Credit tryGet(Uuid player) {
    ensureLookupsInitialized();
    return byUuid.get(player);
  }

  public static Credit tryGet(String name) {
    if (Strings.isNullOrEmpty(name)) return null;

    ensureLookupsInitialized();
    return byName.get(name.toLowerCase());
  }

  public static void initialize() {
    tryLoadCached();
    // TODO: SpreadsheetRequest
  }

  public static boolean syncSheet(String csv) {
    SupporterSheet sheet = SupporterSheet.tryParse(csv);

    if (sheet != null) {
      all = sheet.getCredits();
      supporterSheetLoaded = true;
      sheet.saveToCache();
    }

    return supporterSheetLoaded;
  }

  private static boolean tryLoadCached() {
    try {
      SupporterSheet sheet = SupporterSheet.tryParse(
        Files.readString(Paths.System.getSupportersFile())
      );

      if (sheet != null) {
        all = sheet.getCredits();
        return true;
      }
    } catch (IOException ignored) {
      // Couldn't read cached supporters, move on
    }

    return false;
  }
}
