package org.mcsr.aatool.data.objectives.complex;

import java.util.ArrayList;
import java.util.List;

import org.mcsr.aatool.Time;
import org.mcsr.aatool.data.objectives.ComplexObjective;
import org.mcsr.aatool.data.progress.ProgressState;
import org.mcsr.aatool.utilities.Strings;

public class ArmorTrims extends ComplexObjective {
  public static final List<String> RECIPES = List.of(
    // Required for All Advancements
    "minecraft:recipes/misc/silence_armor_trim_smithing_template",
    "minecraft:recipes/misc/wayfinder_armor_trim_smithing_template",
    "minecraft:recipes/misc/tide_armor_trim_smithing_template",
    "minecraft:recipes/misc/spire_armor_trim_smithing_template",
    "minecraft:recipes/misc/vex_armor_trim_smithing_template",
    "minecraft:recipes/misc/ward_armor_trim_smithing_template",
    "minecraft:recipes/misc/rib_armor_trim_smithing_template",
    "minecraft:recipes/misc/snout_armor_trim_smithing_template",
    // Others
    "minecraft:recipes/misc/raiser_armor_trim_smithing_template",
    "minecraft:recipes/misc/sentry_armor_trim_smithing_template",
    "minecraft:recipes/misc/host_armor_trim_smithing_template",
    "minecraft:recipes/misc/wild_armor_trim_smithing_template",
    "minecraft:recipes/misc/eye_armor_trim_smithing_template",
    "minecraft:recipes/misc/shaper_armor_trim_smithing_template",
    "minecraft:recipes/misc/dune_armor_trim_smithing_template",
    "minecraft:recipes/misc/coast_armor_trim_smithing_template",
    "minecraft:recipes/misc/netherite_upgrade_smithing_template"
  );

  public static final String ADVANCEMENT_ID = "minecraft:adventure/trim_with_all_exclusive_armor_patterns";
  public static final String CATEGORY_ID = "custom:all_smithing_templates";

  private static final double SECONDS_BETWEEN_SWAPS = 5;

  public List<String> required = new ArrayList<>();
  public List<String> remaining = new ArrayList<>();
  public List<String> obtained = new ArrayList<>();
  public List<String> applied = new ArrayList<>();

  private boolean allObtained() { return this.obtained.size() >= required.size() && !required.isEmpty(); }
  private boolean allApplied() { return this.applied.size() >= required.size() && !required.isEmpty(); }
  private boolean isOnLast() { return this.remaining.size() == 1; }

  @Override
  protected void updateAdvancedState(ProgressState progress) {
    this.required.clear();
    // TODO: Tracker
  }

  @Override
  protected void clearAdvancedState() {
    this.remaining.clear();
    // TODO: Tracker
  }

  @Override
  protected String getShortStatus() {
    return this.obtained.size() + "\0/\0" + this.required.size();
  }

  @Override
  protected String getLongStatus() {
    return this.allApplied() ? "All\0Trims\nApplied"
         : this.allObtained() ? "Obtained\nAll\0Trims"
         : this.isOnLast() ? "Still\0Needs\n" + friendlyName(this.remaining.get(0))
         : "Trims\n" + this.obtained.size() + "\0/\0" + this.required.size();
  }

  @Override
  protected String getCurrentIcon() {
    return this.isOnLast() ? iconName(this.remaining.get(0)) : this.icon;
  }

  public final void updateDynamicIcon(Time time) {
    List<String> rotatedList;
    if (this.remaining.size() > 1) rotatedList = this.remaining;
    else if (this.remaining.isEmpty() && !this.required.isEmpty()) rotatedList = this.required;
    else return;

    this.icon = iconName(rotatedList.get(
      (int) (time.getTotalSeconds() / SECONDS_BETWEEN_SWAPS) % rotatedList.size()
    ));
  }

  public static String iconName(String id) {
    return Strings.isNullOrEmpty(id) ? "" : "trim_" + shortName(id);
  }

  public static String friendlyName(String id) {
    if (Strings.isNullOrEmpty(id)) return "";

    String shortName = shortName(id);
    if (shortName.isEmpty()) return "";

    char[] letters = shortName.toCharArray();
    letters[0] = Character.toUpperCase(letters[0]);
    return new String(letters);
  }

  private static String shortName(String id) {
    int colonIndex = id.lastIndexOf(':');
    int underscoreIndex = id.indexOf('_', colonIndex + 1);
    return id.substring(colonIndex + 1, underscoreIndex != -1 ? underscoreIndex : id.length());
  }
}
