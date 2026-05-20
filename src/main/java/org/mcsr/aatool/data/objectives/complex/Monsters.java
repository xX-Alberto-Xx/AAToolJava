package org.mcsr.aatool.data.objectives.complex;

import java.util.Set;

import org.mcsr.aatool.data.objectives.CriteriaSet;
import org.mcsr.aatool.data.objectives.MultipartObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Monsters extends MultipartObjective {
  private static final String[] RAID_MOBS;

  protected final Set<String> remainingNonRaidMobs;

  public Monsters() {}

  private boolean onlyRaidMobsLeft() {}

  private boolean onlyRaidMobsPlusOneLeft() {}

  @Override
  public String getAdvancementId() {}
  @Override
  public String getCriterion() {}
  @Override
  public String getAction() {}
  @Override
  public String getPastAction() {}
  @Override
  protected String getModernBaseTexture() {}
  @Override
  protected String getOldBaseTexture() {}

  @Override
  protected void updateAdvancedState(ProgressState progress) {}

  @Override
  protected void buildRemainingCriteriaList(CriteriaSet criteria) {}

  @Override
  protected void clearAdvancedState() {}

  @Override
  protected String getLongStatus() {}

  private String formatMobName(String name) {}

  @Override
  protected String longStatusNormal() {}

  @Override
  protected String getCurrentIcon() {}
}
