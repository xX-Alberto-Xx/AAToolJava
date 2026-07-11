package org.mcsr.aatool.net;

import org.mcsr.aatool.utilities.Result;
import org.mcsr.aatool.utilities.Strings;

public class User {
  private static final int MAX_NAME_LENGTH = 16;
  private static final String ELLIPSIS = "...";

  public static final User NOBODY = new User(Uuid.EMPTY, "", "");

  public final Uuid id;
  public final String pronouns;
  private final String preferredName;

  public User(Uuid id, String pronouns) { this(id, pronouns, null); }
  public User(Uuid id, String pronouns, String preferredName) {
    this.id = id;
    this.pronouns = pronouns;

    if (preferredName != null && preferredName.length() > MAX_NAME_LENGTH) {
      // Abbreviate name if too long
      this.preferredName = preferredName.substring(0, MAX_NAME_LENGTH - ELLIPSIS.length()) + ELLIPSIS;
    } else if (Strings.isNullOrBlank(preferredName)) {
      // Set preferred name to real name if not provided and real name is known
      Result<String> realName = Player.tryGetName(new Uuid(preferredName));
      this.preferredName = realName.success ? realName.value : preferredName;
    } else {
      this.preferredName = preferredName;
    }
  }

  @Override
  public int hashCode() { return this.id.hashCode(); }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof User user && this.id.equals(user.id);
  }

  public final String getName() {
    if (!Strings.isNullOrEmpty(this.preferredName)) return this.preferredName;

    Result<String> name = Player.tryGetName(this.id);
    return name.success ? name.value : this.preferredName;
  }
}
