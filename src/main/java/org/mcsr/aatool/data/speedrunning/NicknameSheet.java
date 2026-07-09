package org.mcsr.aatool.data.speedrunning;

import java.util.HashMap;
import java.util.Map;

import org.mcsr.aatool.net.Uuid;

public class NicknameSheet extends Spreadsheet {
  private final int nickNameCol;
  private final int realNameCol;
  private final int uuidCol;

  private NicknameSheet(String csv) {
    super(csv, "leaderboard_names");

    // Find column headers
    this.nickNameCol = this.find("name", "nickname", "nick", "preferred").x;
    this.realNameCol = this.find("ign", "ingame name", "in-game name", "minecraft name", "mojang name").x;
    this.uuidCol = this.find("uuid", "guid").x;

    this.isValid = this.nickNameCol >= 0 && (this.realNameCol >= 0 || this.uuidCol >= 0);
  }

  public static NicknameSheet tryParse(String csv) {
    NicknameSheet sheet = new NicknameSheet(csv);
    return sheet.isValid ? sheet : null;
  }

  public final Mappings getMappings() {
    Mappings mappings = new Mappings();
    int numRows = this.getRows().length;

    for (int i = 1; i < numRows; i++) {
      String nick = this.tryGetNickname(i);
      if (nick.isEmpty()) continue;

      Uuid uuid = this.tryGetUuid(i);

      if (uuid != null) {
        mappings.identities.put(nick.toLowerCase(), uuid);
        mappings.nickNames.put(uuid.string, nick);
      }

      String real = this.tryGetRealName(i);

      if (!real.isEmpty()) {
        if (uuid != null && !uuid.equals(Uuid.EMPTY)) {
          mappings.identities.put(real.toLowerCase(), uuid);
          mappings.realNames.put(uuid.string, real);
        }

        mappings.realNames.put(nick.toLowerCase(), real);
        mappings.nickNames.put(real.toLowerCase(), nick);
      }
    }

    return mappings;
  }

  public final Uuid tryGetUuid(int index) {
    return Uuid.tryParse(this.tryGetCell(index, this.uuidCol).strip());
  }

  public final String tryGetNickname(int index) {
    return this.tryGetCell(index, this.nickNameCol).strip();
  }

  public final String tryGetRealName(int index) {
    return this.tryGetCell(index, this.realNameCol).strip();
  }

  public static final class Mappings {
    public final Map<String, String> realNames = new HashMap<>();
    public final Map<String, String> nickNames = new HashMap<>();
    public final Map<String, Uuid> identities = new HashMap<>();
  }
}
