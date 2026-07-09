package org.mcsr.aatool.data.objectives.complex;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.configuration.Config;
import org.mcsr.aatool.data.categories.AllAchievements;
import org.mcsr.aatool.data.categories.Category;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Version;

public class EGap extends ComplexObjective {
  public static final String ITEM_ID = "minecraft:enchanted_golden_apple";
  public static final String BALANCED_DIET = "minecraft:husbandry/balanced_diet";
  public static final String OVERPOWERED = "achievement.overpowered";
  public static final String ENCHANTED_GOLDEN_APPLE = "enchanted_golden_apple";
  public static final String BANNER_RECIPE = "minecraft:recipes/misc/mojang_banner_pattern";

  private static final Version TEXTURE_CHANGED = new Version(1, 14);
  private static final Version ID_ADDED = new Version(1, 12);

  private boolean looted;
  private boolean eaten;

  public final boolean isLooted() { return this.looted; }
  public final boolean isEaten() { return this.eaten; }

  private static boolean useModernTexture() {
    Version current = Version.tryParse(Tracker.getCurrentVersion());
    return current == null || current.isAtLeast(TEXTURE_CHANGED);
  }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.looted = progress.obtainedGodApple;

    this.eaten = Tracker.getCategory() instanceof AllAchievements
               ? progress.advancementCompleted(OVERPOWERED)
               : progress.criterionCompleted(BALANCED_DIET, ENCHANTED_GOLDEN_APPLE);

    Version current = Version.tryParse(Tracker.getCategory().getCurrentMajorVersion());

    if (current != null) {
      this.canBeManuallyChecked = current.isAtMost(ID_ADDED) && !(this.looted || this.eaten);
    }

    this.completionOverride = this.looted || this.eaten || this.manuallyChecked;
  }

  @Override
  protected void clearAdvancedState() {
    this.looted = false;
    this.eaten = false;

    Category category = Tracker.getCategory();
    if (category == null) return;

    Version current = Version.tryParse(category.getCurrentMajorVersion());

    if (current != null) {
      this.canBeManuallyChecked = current.isAtMost(ID_ADDED) && !(this.looted || this.eaten);
    }
  }

  @Override
  protected String getShortStatus() {
    return this.eaten ? "Eaten"
         : this.looted ? "Obtained"
         : Config.getMain().renameToNotchApple.getValue() ? "Notch\0Apple" : "God\0Apple";
  }

  @Override
  protected String getLongStatus() {
    String adjunct = Config.getMain().renameToNotchApple.getValue() ? "Notch" : "God";

    return this.eaten ? adjunct + "\0Apple\nEaten"
         : this.looted || this.manuallyChecked ? "Obtained\n" + adjunct + "\0Apple"
         : "Obtain\n" + adjunct + "\0Apple";
  }

  @Override
  protected String getCurrentIcon() {
    return useModernTexture() ? "enchanted_golden_apple" : "enchanted_golden_apple_1.12";
  }
}
