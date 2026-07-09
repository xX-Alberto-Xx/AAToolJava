package org.mcsr.aatool.configuration;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.util.Map;

import org.mcsr.aatool.Tracker;
import org.mcsr.aatool.data.categories.AllAdvancements;
import org.mcsr.aatool.data.categories.AllBlocks;
import org.mcsr.aatool.data.categories.Category;
import org.mcsr.aatool.enums.WindowSnap;
import org.mcsr.aatool.utilities.ColorHelper;
import org.mcsr.aatool.utilities.Point;
import org.mcsr.aatool.utilities.Strings;
import org.mcsr.aatool.utilities.Version;

public class MainConfig extends Config {
  private static final Version VERTICAL_MONITOR_UPDATE = new Version(1, 4, 5, 0);

  public static final String RELAXED_LAYOUT = "relaxed";
  public static final String COMPACT_LAYOUT = "compact";
  public static final String VERTICAL_LAYOUT = "vertical";
  public static final String OPTIMIZED_LAYOUT = "optimized";

  public static final String AUTO_SWITCH_PANEL = "Auto-Switch";
  public static final String RUN_OVERVIEW_PANEL = "Run Overview";
  public static final String LEADERBOARD_PANEL = "Leaderboard";
  public static final String BREWING_PANEL = "Potion Recipes";

  public static final Map<String, Theme> THEME = Map.of(
    "Dark Mode", new Theme(hex("36393F"), hex("DCDDDE"), hex("4E5156")),
    "Light Mode", new Theme(hex("F0F0F0"), hex("000000"), hex("C4C4C4")),
    "GitHub Dark", new Theme(hex("0D1117"), hex("C9D1D9"), hex("30363D")),
    "Ender Pearl", new Theme(hex("0C3730"), hex("C6F2EA"), hex("349988")),
    "Blazed", new Theme(hex("91360B"), hex("FFFFCC"), hex("E4871F")),
    "Brick", new Theme(hex("804040"), hex("FFFFFF"), hex("AA5A5A")),
    "Berry", new Theme(hex("411126"), hex("C9D1D9"), hex("5E1938")),
    "Couri Enjoyer", new Theme(hex("502880"), hex("C9D1D9"), hex("8336B6")),
    "90's Hacker", new Theme(hex("001800"), hex("00FF00"), hex("005000")),
    "High Contrast", new Theme(hex("000000"), hex("FFFFFF"), hex("FFFFFF"))
  );

  public final Setting<Integer> fpsCap = new Setting<>(60);
  public final Setting<Integer> displayScale = new Setting<>(1);

  public final Setting<Boolean> allowUserResizing = new Setting<>(false);
  public final Setting<Boolean> hideCompletedAdvancements = new Setting<>(false);
  public final Setting<Boolean> hideCompletedCriteria = new Setting<>(false);
  public final Setting<Boolean> showBasicAdvancements = new Setting<>(true);
  public final Setting<Boolean> showAmbientGlow = new Setting<>(true);
  public final Setting<Boolean> showMyBadge = new Setting<>(true);
  public final Setting<Boolean> rainbowMode = new Setting<>(false);
  public final Setting<Boolean> closeFramesOnSelection = new Setting<>(true);

  public final Setting<String> layout = new Setting<>(monitorSupportsRelaxed() ? RELAXED_LAYOUT : COMPACT_LAYOUT);

  public final Setting<Boolean> layoutDebugMode = new Setting<>(false);
  public final Setting<Boolean> cacheDebugMode = new Setting<>(false);
  public final Setting<Boolean> hideRenderCache = new Setting<>(false);
  public final Setting<Boolean> hideGlowEffects = new Setting<>(false);

  public final Setting<String> frameStyle = new Setting<>("Modern");
  public final Setting<String> prideFrameList = new Setting<>("");
  public final Setting<String> progressBarStyle = new Setting<>("Modern");
  public final Setting<String> refreshIcon = new Setting<>("Xp Orb");
  public final Setting<String> infoPanel = new Setting<>("Leaderboard");

  public final Setting<String> preferredPlayerBadge = new Setting<>("Default");
  public final Setting<String> preferredPlayerFrame = new Setting<>("Default");

  public final Setting<Color> backColor = new Setting<>(hex("36393F"));
  public final Setting<Color> textColor = new Setting<>(hex("DCDDDE"));
  public final Setting<Color> borderColor = new Setting<>(hex("4E5156"));

  public final Setting<WindowSnap> startupArrangement = new Setting<>(WindowSnap.CENTERED);
  public final Setting<Point> lastWindowPosition = new Setting<>(Point.ZERO);
  public final Setting<Integer> startupDisplay = new Setting<>(1);
  public final Setting<Boolean> alwaysOnTop = new Setting<>(false);

  public final Setting<Boolean> renameToNotchApple = new Setting<>(false);

  // Deprecated (now used to migrate preference from pre-1.4.5.0)
  public final Setting<Boolean> compactMode = new Setting<>(false);

  private String[] prideStyles;

  public MainConfig() {
    this.registerSetting(this.layout);
    this.registerSetting(this.fpsCap);
    this.registerSetting(this.displayScale);

    this.registerSetting(this.allowUserResizing);

    this.registerSetting(this.hideCompletedAdvancements);
    this.registerSetting(this.hideCompletedCriteria);

    this.registerSetting(this.showBasicAdvancements);
    this.registerSetting(this.showAmbientGlow);
    this.registerSetting(this.showMyBadge);

    this.registerSetting(this.compactMode);
    this.registerSetting(this.rainbowMode);

    this.registerSetting(this.layoutDebugMode);
    this.registerSetting(this.cacheDebugMode);
    this.registerSetting(this.hideGlowEffects);

    this.registerSetting(this.frameStyle);
    this.registerSetting(this.prideFrameList);
    this.registerSetting(this.progressBarStyle);
    this.registerSetting(this.refreshIcon);
    this.registerSetting(this.infoPanel);

    this.registerSetting(this.preferredPlayerBadge);
    this.registerSetting(this.preferredPlayerFrame);

    this.registerSetting(this.backColor);
    this.registerSetting(this.textColor);
    this.registerSetting(this.borderColor);

    this.registerSetting(this.startupArrangement);
    this.registerSetting(this.startupDisplay);
    this.registerSetting(this.lastWindowPosition);
    this.registerSetting(this.alwaysOnTop);
    this.registerSetting(this.renameToNotchApple);
  }

  private static Color hex(String hex) {
    Color color = ColorHelper.tryGetHexColor(hex);
    return color != null ? color : Color.WHITE;
  }

  public final boolean useCompactStyling() {
    return (
      COMPACT_LAYOUT.equals(this.layout.getValue()) || this.useOptimizedLayout()
    ) && !(Tracker.getCategory() instanceof AllBlocks);
  }

  public final boolean useVerticalStyling() { return VERTICAL_LAYOUT.equals(this.layout.getValue()); }

  public final boolean useOptimizedLayout() {
    if (!OPTIMIZED_LAYOUT.equals(this.layout.getValue())) return false;

    Category category = Tracker.getCategory();
    return category != null && category.getClass() == AllAdvancements.class;
  }

  public final boolean isAppearanceChanged() {
    return this.frameStyle.isChanged()
        || this.borderColor.isChanged()
        || this.backColor.isChanged()
        || this.textColor.isChanged()
        || this.progressBarStyle.isChanged()
        || this.prideFrameList.isChanged();
  }

  private static boolean monitorSupportsRelaxed() {
    DisplayMode primaryMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    return primaryMode.getWidth() >= 1600 && primaryMode.getHeight() >= 900;
  }

  @Override
  protected String getId() { return "main"; }

  public final void setPrideList(String csv) {
    this.prideFrameList.set(csv);
    this.prideStyles = csv.split(",");
  }

  public final String getActiveFrameStyle(int x, int y) {
    if (this.prideStyles == null) this.prideStyles = this.prideFrameList.getValue().split(",");

    if (!"Multi-Pride".equals(this.frameStyle.getValue()) || this.prideStyles.length == 0) {
      return this.frameStyle.getValue();
    }

    boolean compact = COMPACT_LAYOUT.equals(this.layout.getValue());
    int col = x / (compact ? 60 : 68);
    int row = y / (compact ? 72 : 84);
    return this.prideStyles[(col + row) % this.prideStyles.length];
  }

  @Override
  protected void migrateDeprecatedConfigs() {
    if (Strings.isNullOrBlank(this.layout.getValue())) this.layout.applyDefault();
    if (Strings.isNullOrBlank(this.frameStyle.getValue())) this.frameStyle.applyDefault();
    if (Strings.isNullOrBlank(this.progressBarStyle.getValue())) this.progressBarStyle.applyDefault();
    if (Strings.isNullOrBlank(this.infoPanel.getValue())) this.infoPanel.applyDefault();
    if (Strings.isNullOrBlank(this.refreshIcon.getValue())) this.refreshIcon.applyDefault();

    // Migrate relaxed vs compact preference from pre-1.4.5.0 installation
    Version last = Version.tryParse(getTracking().lastSession.getValue());

    if (last != null && last.isBefore(VERTICAL_MONITOR_UPDATE) && this.compactMode.getValue()) {
      this.layout.set(COMPACT_LAYOUT);
      this.compactMode.set(false);
      this.trySave();
    }
  }

  public record Theme(Color back, Color text, Color border) {}
}
