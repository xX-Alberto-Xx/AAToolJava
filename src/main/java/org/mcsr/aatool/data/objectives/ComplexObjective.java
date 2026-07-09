package org.mcsr.aatool.data.objectives;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.objectives.complex.*;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.enums.FrameType;

public abstract class ComplexObjective extends Objective {
  // Static map to hold all complex objective types for dynamic instantiation
  public static final Map<String, Class<? extends ComplexObjective>> TYPES = Stream.of(
    AncientDebris.class, Animals.class,
    ArmorTrims.class, Bees.class,
    Biomes.class, Cats.class,
    Cauldrons.class, DeepslateEmerald.class,
    EGap.class, EGapShort.class,
    EnderPearls.class, Foods.class,
    GhastTears.class, GoldBlocks.class,
    HeavyCore.class, Monsters.class,
    Mycelium.class, NautilusShells.class,
    Netherite.class, NetheriteUpgrade.class,
    PotterySherds.class, PrismarineBlocks.class,
    Pufferfish.class, RedSand.class,
    SculkBlocks.class, ShulkerShells.class,
    SnifferEgg.class, Sniffers.class,
    Tnt.class, Trident.class,
    TridentAdvancements.class, WaxOnOff.class,
    WitherSkulls.class
  ).collect(Collectors.toUnmodifiableMap(
    c -> c.getSimpleName().toLowerCase(),
    Function.identity()
  ));

  private String fullStatus;
  private String tinyStatus;

  public ComplexObjective() {
    super(null);
    this.name = this.getClass().getSimpleName();
    this.frame = FrameType.STATISTIC;

    this.clearAdvancedState();
    this.refreshStatus();
  }

  public static ComplexObjective tryCreateInstance(String type) {
    // If type name is valid, create instance of type
    Class<? extends ComplexObjective> realType = TYPES.get(type);
    if (realType == null) return null;

    try {
      return realType.getConstructor().newInstance();
    } catch (ReflectiveOperationException e) {
      throw new AssertionError(realType.getSimpleName() + " cannot be instantiated", e);
    }
  }

  @Override
  public String getFullStatus() { return this.fullStatus; }
  @Override
  public String getTinyStatus() { return this.tinyStatus; }

  protected abstract String getLongStatus();
  protected abstract String getShortStatus();
  protected abstract void updateAdvancedState(ProgressState progress);
  protected abstract void clearAdvancedState();

  protected String getCurrentIcon() { return this.icon; }

  @Override
  public final void updateState(ProgressState progress) {
    if (Tracker.isWorldChanged() || Tracker.isSavesFolderChanged() || !Tracker.isWorking()) {
      this.manuallyChecked = false;
    }

    if (progress != null) {
      this.updateAdvancedState(progress);
    } else {
      this.clearAdvancedState();
      this.completionOverride = this.manuallyChecked;
    }

    this.refreshStatus();
  }

  public final void refreshStatus() {
    this.fullStatus = this.getLongStatus();
    this.tinyStatus = this.getShortStatus();
    this.icon = this.getCurrentIcon();
  }
}
