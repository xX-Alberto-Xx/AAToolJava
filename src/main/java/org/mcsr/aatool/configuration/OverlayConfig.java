package org.mcsr.aatool.configuration;

import org.mcsr.aatool.enums.WindowSnap;

public class OverlayConfig extends Config {
  public final Setting<Boolean> enabled;
  public final Setting<Boolean> showLabels;
  public final Setting<Boolean> showCriteria;
  public final Setting<Boolean> showPickups;
  public final Setting<Boolean> showIgt;
  public final Setting<Boolean> showLastRefresh;
  public final Setting<Boolean> rightToLeft;
  public final Setting<Boolean> pickupsOpposite;
  public final Setting<Boolean> lastRefreshOpposite;
  public final Setting<Boolean> clarifyAmbiguous;

  public final Setting<String> position;
  public final Setting<String> frameStyle;
  public final Setting<String> prideFrameList;

  public final Setting<PinnedObjectiveSet> pinnedObjectiveList;

  public final Setting<Integer> speed;
  public final Setting<Integer> width;

  public final Setting<Color> greenScreen;
  public final Setting<Color> customTextColor;
  public final Setting<Color> customBackColor;
  public final Setting<Color> customBorderColor;

  public final Setting<WindowSnap> startupArrangement;
  public final Setting<Point> lastWindowPosition;
  public final Setting<Integer> startupDisplay;

  private String[] prideStyles;
  private int styleIndex;

  public OverlayConfig() {}

  public final boolean isAppearanceChanged() {}

  public final boolean isArrangementChanged() {}

  @Override
  protected String getId() {}

  private static Color hex(String hex) {}

  @Override
  protected void applyDefaultValues() {}

  public final void setPrideList(String csv) {}

  public final String getActiveFrameStyle(String currentStyle) {}
}
