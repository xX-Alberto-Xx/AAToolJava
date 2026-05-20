package org.mcsr.aatool.data.objectives.complex;

import java.util.Set;

import org.mcsr.aatool.data.objectives.CriteriaSet;
import org.mcsr.aatool.data.objectives.MultipartObjective;
import org.mcsr.aatool.data.progress.ProgressState;

public class Biomes extends MultipartObjective {
  private static final String[] MEGA_TAIGA_BIOMES;

  private static final String[] MUSHROOM_BIOMES;

  private static final String[] BADLANDS_BIOMES;

  private static final String[] BAMBOO_BIOMES;

  private boolean onlyMushroomLeft;
  private boolean onlyMegaTaigaLeft;
  private boolean onlyBadlandsLeft;
  private boolean onlyBambooLeft;

  protected final Set<String> remainingIds;

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

  private boolean onlyGroupRemaining(String[] group, int maxRemaining) {}

  @Override
  protected void clearAdvancedState() {}

  private static String formatBiomeName(String name) {}

  @Override
  protected String getLongStatus() {}

  @Override
  protected String longStatusNormal() {}

  @Override
  protected String longStatusLast() {}

  @Override
  protected String getCurrentIcon() {}
}
