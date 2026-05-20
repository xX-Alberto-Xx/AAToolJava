package org.mcsr.aatool.data.speedrunning;

import java.util.Map;

import org.mcsr.aatool.net.Uuid;

public class NicknameSheet extends Spreadsheet {
  private final int nickNameCol;
  private final int realNameCol;
  private final int uuidCol;

  private NicknameSheet(String csv) {}

  public static boolean tryParse(String csv, /*out */NicknameSheet sheet) {}

  public final void getMappings(
    /*out */Map<String, String> realNames,
    /*out */Map<String, String> nickNames,
    /*out */Map<String, Uuid> identities
  ) {}

  public final boolean tryGetUuid(int index, /*out */Uuid uuid) {}

  public final boolean tryGetNickname(int index, /*out */String nickName) {}

  public final boolean tryGetRealName(int index, /*out */String realName) {}
}
