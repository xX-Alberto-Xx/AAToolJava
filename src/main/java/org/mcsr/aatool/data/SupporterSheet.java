package org.mcsr.aatool.data;

import java.util.List;
import java.util.Set;

import org.mcsr.aatool.data.speedrunning.Spreadsheet;
import org.mcsr.aatool.net.Uuid;

public class SupporterSheet extends Spreadsheet {
  private final int activeCol;
  private final int currentTierCol;
  private final int highestTierCol;
  private final int nameCol;
  private final int uuidsCol;
  private final int altNamesCol;

  public SupporterSheet(String csv) {}

  public static boolean tryParse(String csv, /*out */SupporterSheet sheet) {}

  public final void getCredits(/*out */Set<Credit> credits) {}

  public final boolean tryGetName(int index, /*out */String name) {}

  public final boolean tryGetActive(int index, /*out */boolean active) {}

  public final boolean tryGetCurrentTier(int index, /*out */String currentTier) {}

  public final boolean tryGetHighestTier(int index, /*out */String highestTier) {}

  public final boolean tryGetUuids(int index, /*out */List<Uuid> uuids) {}

  public final boolean tryGetAltNames(int index, /*out */List<String> altNames) {}
}
