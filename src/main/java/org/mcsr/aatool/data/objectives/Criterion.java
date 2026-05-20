package org.mcsr.aatool.data.objectives;

import org.mcsr.aatool.net.Uuid;

public class Criterion extends Objective {
  public final Advancement owner;

  public Criterion(XmlNode node, Advancement advancement) {}

  public static String key(String advancement, String criterion) {}

  public final Uuid getDesignatedPlayer() {}
  public final String getOwnerId() {}

  public boolean completedByDesignated() {}

  @Override
  public boolean isComplete() {}

  @Override
  public String getFullStatus() {}
  @Override
  public String getTinyStatus() {}
}
