package org.mcsr.aatool.configuration;

import java.awt.Color;

import org.mcsr.aatool.enums.WindowSnap;
import org.mcsr.aatool.utilities.ColorHelper;
import org.mcsr.aatool.utilities.Point;
import org.mcsr.aatool.utilities.SearchUtils;
import org.mcsr.aatool.utilities.Strings;

public class OverlayConfig extends Config {
  public final Setting<Boolean> enabled = new Setting<>(false);
  public final Setting<Boolean> showLabels = new Setting<>(true);
  public final Setting<Boolean> showCriteria = new Setting<>(true);
  public final Setting<Boolean> showPickups = new Setting<>(true);
  public final Setting<Boolean> showIgt = new Setting<>(false);
  public final Setting<Boolean> showLastRefresh = new Setting<>(true);
  public final Setting<Boolean> rightToLeft = new Setting<>(false);
  public final Setting<Boolean> pickupsOpposite = new Setting<>(false);
  public final Setting<Boolean> lastRefreshOpposite = new Setting<>(false);
  public final Setting<Boolean> clarifyAmbiguous = new Setting<>(true);

  public final Setting<String> position = new Setting<>("Minecraft");
  public final Setting<String> frameStyle = new Setting<>("Minecraft");
  public final Setting<String> prideFrameList = new Setting<>("");

  public final Setting<PinnedObjectiveSet> pinnedObjectiveList = new Setting<>(new PinnedObjectiveSet());

  public final Setting<Integer> speed = new Setting<>(2);
  public final Setting<Integer> width = new Setting<>(1920);

  public final Setting<Color> greenScreen = new Setting<>(new Color(0, 170, 0));
  public final Setting<Color> customTextColor = new Setting<>(hex("FFFFFF"));
  public final Setting<Color> customBackColor = new Setting<>(hex("FFFFFF"));
  public final Setting<Color> customBorderColor = new Setting<>(hex("FFFFFF"));

  public final Setting<WindowSnap> startupArrangement = new Setting<>(WindowSnap.TOP_LEFT);
  public final Setting<Point> lastWindowPosition = new Setting<>(Point.ZERO);
  public final Setting<Integer> startupDisplay = new Setting<>(1);

  private String[] prideStyles;
  private int styleIndex;

  public OverlayConfig() {
    this.registerSetting(this.enabled);
    this.registerSetting(this.showLabels);
    this.registerSetting(this.showCriteria);
    this.registerSetting(this.showPickups);
    this.registerSetting(this.showLastRefresh);

    this.registerSetting(this.rightToLeft);
    this.registerSetting(this.pickupsOpposite);
    this.registerSetting(this.lastRefreshOpposite);
    this.registerSetting(this.frameStyle);
    this.registerSetting(this.prideFrameList);

    this.registerSetting(this.pinnedObjectiveList);

    this.registerSetting(this.speed);
    this.registerSetting(this.width);

    this.registerSetting(this.greenScreen);
    this.registerSetting(this.customTextColor);
    this.registerSetting(this.customBackColor);
    this.registerSetting(this.customBorderColor);

    this.registerSetting(this.showIgt);
    this.registerSetting(this.clarifyAmbiguous);

    this.registerSetting(this.startupArrangement);
    this.registerSetting(this.startupDisplay);
    this.registerSetting(this.lastWindowPosition);
  }

  public final boolean isAppearanceChanged() {
    return this.enabled.isChanged()
        || this.frameStyle.isChanged()
        || this.customBackColor.isChanged()
        || this.customBorderColor.isChanged();
  }

  public final boolean isArrangementChanged() {
    return this.enabled.isChanged()
        || this.rightToLeft.isChanged()
        || this.pickupsOpposite.isChanged()
        || this.showLastRefresh.isChanged()
        || this.lastRefreshOpposite.isChanged();
  }

  @Override
  protected String getId() { return "overlay"; }

  private static Color hex(String hex) {
    Color color = ColorHelper.tryGetHexColor(hex);
    return color != null ? color : Color.WHITE;
  }

  @Override
  protected void applyDefaultValues() {
    super.applyDefaultValues();
    this.pinnedObjectiveList.set(new PinnedObjectiveSet());
  }

  public final void setPrideList(String csv) {
    this.prideFrameList.set(csv);
    this.prideStyles = csv.split(",");
  }

  public final String getActiveFrameStyle(String currentStyle) {
    if (this.prideStyles == null) this.prideStyles = this.prideFrameList.getValue().split(",");

    if (!"Multi-Pride".equals(this.frameStyle.getValue()) || this.prideStyles.length == 0) {
      return this.frameStyle.getValue();
    }

    if (!Strings.isNullOrEmpty(currentStyle) && SearchUtils.contains(this.prideStyles, currentStyle)) {
      return currentStyle;
    }

    String style = this.prideStyles[this.styleIndex];
    this.styleIndex = (this.styleIndex + 1) % this.prideStyles.length;
    return style;
  }
}
