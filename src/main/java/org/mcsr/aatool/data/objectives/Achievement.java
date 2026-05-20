package org.mcsr.aatool.data.objectives;

import java.util.Map;

public class Achievement extends Advancement {
  public final Map<String, Achievement> children;
  public final Achievement parent;

  public Achievement(XmlNode node, Achievement parent/* = null*/) {}

  public final boolean isRoot() {}
  public final boolean isLocked() {}

  public final Map<String, Advancement> getAllChildrenRecursive(Map<String, Advancement> children) {}
}
