package org.mcsr.aatool.data;

import java.util.ArrayList;
import java.util.HashSet;
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

  public SupporterSheet(String csv) {
    super(csv, "supporters");

    this.nameCol = this.find("name").x;
    this.activeCol = this.find("active").x;
    this.currentTierCol = this.find("current tier").x;
    this.highestTierCol = this.find("highest tier").x;
    this.uuidsCol = this.find("uuid").x;
    this.altNamesCol = this.find("alt names").x;

    this.isValid = this.nameCol >= 0
                && this.activeCol >= 0
                && this.currentTierCol >= 0
                && this.highestTierCol >= 0;
  }

  public static SupporterSheet tryParse(String csv) {
    SupporterSheet sheet = new SupporterSheet(csv);
    return sheet.isValid ? sheet : null;
  }

  public final Set<Credit> getCredits() {
    Set<Credit> credits = new HashSet<>();
    int numRows = this.getRows().length;

    for (int i = 1; i < numRows; i++) {
      String name = this.tryGetName(i);
      if (name.isEmpty()) continue;
      if (this.tryGetActive(i) == null) continue;

      String highestTier = this.tryGetHighestTier(i);
      if (highestTier.isEmpty()) continue;

      credits.add(new Credit(
        highestTier,
        this.tryGetCurrentTier(i),
        name,
        this.tryGetAltNames(i),
        this.tryGetUuids(i)
      ));
    }

    return credits;
  }

  public final String tryGetName(int index) {
    return this.tryGetCell(index, this.nameCol).strip();
  }

  public final Boolean tryGetActive(int index) {
    return this.tryGetBoolean(index, this.activeCol);
  }

  public final String tryGetCurrentTier(int index) {
    return this.tryGetCell(index, this.currentTierCol).strip();
  }

  public final String tryGetHighestTier(int index) {
    return this.tryGetCell(index, this.highestTierCol).strip();
  }

  public final List<Uuid> tryGetUuids(int index) {
    String uuidListString = this.tryGetCell(index, this.uuidsCol);
    if (uuidListString.isEmpty()) return List.of();

    List<Uuid> uuids = new ArrayList<>();

    for (String item : uuidListString.split(" ")) {
      Uuid uuid = Uuid.tryParse(item);
      if (uuid != null) uuids.add(uuid);
    }

    return uuids;
  }

  public final List<String> tryGetAltNames(int index) {
    String altNameListString = this.tryGetCell(index, this.altNamesCol);
    if (altNameListString.isEmpty()) return List.of();

    List<String> altNames = new ArrayList<>();
    for (String item : altNameListString.split(" ")) altNames.add(item.strip());
    return altNames;
  }
}
