package org.mcsr.aatool.configuration;

import java.util.Map;

import org.mcsr.aatool.enums.WindowSnap;

public class MainConfig extends Config {
  private static final Version VERTICAL_MONITOR_UPDATE;

  public static final String RELAXED_LAYOUT;
  public static final String COMPACT_LAYOUT;
  public static final String VERTICAL_LAYOUT;
  public static final String OPTIMIZED_LAYOUT;

  public static final String AUTO_SWITCH_PANEL;
  public static final String RUN_OVERVIEW_PANEL;
  public static final String LEADERBOARD_PANEL;
  public static final String BREWING_PANEL;

  public static final Map<String, Theme> THEME;

  public final Setting<Integer> fpsCap;
  public final Setting<Integer> displayScale;

  public final Setting<Boolean> allowUserResizing;
  public final Setting<Boolean> hideCompletedAdvancements;
  public final Setting<Boolean> hideCompletedCriteria;
  public final Setting<Boolean> showBasicAdvancements;
  public final Setting<Boolean> showAmbientGlow;
  public final Setting<Boolean> showMyBadge;
  public final Setting<Boolean> rainbowMode;
  public final Setting<Boolean> closeFramesOnSelection;

  public final Setting<String> layout;

  public final Setting<Boolean> layoutDebugMode;
  public final Setting<Boolean> cacheDebugMode;
  public final Setting<Boolean> hideRenderCache;
  public final Setting<Boolean> hideGlowEffects;

  public final Setting<String> frameStyle;
  public final Setting<String> prideFrameList;
  public final Setting<String> progressBarStyle;
  public final Setting<String> refreshIcon;
  public final Setting<String> infoPanel;

  public final Setting<String> preferredPlayerBadge;
  public final Setting<String> preferredPlayerFrame;

  public final Setting<Color> backColor;
  public final Setting<Color> textColor;
  public final Setting<Color> borderColor;

  public final Setting<WindowSnap> startupArrangement;
  public final Setting<Point> lastWindowPosition;
  public final Setting<Integer> startupDisplay;
  public final Setting<Boolean> alwaysOnTop;

  public final Setting<Boolean> renameToNotchApple;

  public final Setting<Boolean> compactMode;

  private String[] prideStyles;

  public MainConfig() {}

  private static Color hex(String hex) {}

  public final boolean useCompactStyling() {}

  public final boolean useVerticalStyling() {}

  public final boolean useOptimizedLayout() {}

  public final boolean isAppearanceChanged() {}

  private static boolean monitorSupportsRelaxed() {}

  @Override
  protected String getId() {}

  public final void setPrideList(String csv) {}

  public final String getActiveFrameStyle(int x, int y) {}

  @Override
  protected void migrateDeprecatedConfigs() {}

  private record Theme(Color back, Color text, Color border) {}
}
