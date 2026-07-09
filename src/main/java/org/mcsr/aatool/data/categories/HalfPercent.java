package org.mcsr.aatool.data.categories;

import java.util.List;

public class HalfPercent extends AllAdvancements {
  public static final List<String> SUPPORTED_VERSIONS_HALF = List.of("1.16");

  public HalfPercent() {
    this.name = "Half Percent";
    this.acronym = "HP";
  }

  @Override
  public Iterable<String> getSupportedVersions() { return SUPPORTED_VERSIONS_HALF; }

  @Override
  public int getTargetCount() { return (super.getTargetCount() + 1) / 2; }

  @Override
  public String getCompletionMessage() {
    return "Half (" + this.getTargetCount() + ") of All " + this.objective + ' ' + this.action + '!';
  }
}
