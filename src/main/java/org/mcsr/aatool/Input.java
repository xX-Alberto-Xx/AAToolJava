package org.mcsr.aatool;

public final class Input {
  private static boolean isActive;

  private static MouseState mouseNow;
  private static MouseState mousePrev;
  private static KeyboardState keyboardNow;
  private static KeyboardState keyboardPrev;

  private static Keys[] keysNow;
  private static Keys[] keysPrev;

  private static int scrollNow;
  private static int scrollPrev;
  private static boolean capsLock;

  private Input() {}

  public static boolean isActive() { return isActive; }

  public static MouseState getMouseNow() { return mouseNow; }
  public static MouseState getMousePrev() { return mousePrev; }
  public static KeyboardState getKeyboardNow() { return keyboardNow; }
  public static KeyboardState getKeyboardPrev() { return keyboardPrev; }

  public static Keys[] getKeysNow() { return keysNow; }
  public static Keys[] getKeysPrev() { return keysPrev; }

  public static int getScrollNow() { return scrollNow; }
  public static int getScrollPrev() { return scrollPrev; }
  public static boolean getCapsLock() { return capsLock; }

  public static boolean isLeftClicking() {}
  public static boolean isLeftClicked() {}
  public static boolean isLeftClickStarted() {}
  public static boolean isRightClicking() {}
  public static boolean isRightClicked() {}
  public static boolean isRightClickStarted() {}

  public static Point cursor(UIScreen screen) {}

  public static boolean isDown(Keys key) {}
  public static boolean wasDown(Keys key) {}
  public static boolean ended(Keys key) {}
  public static boolean started(Keys key) {}

  public static boolean scrolledUp() {}
  public static boolean scrolledDown() {}

  public static void supressClicks() {}

  public static void beginUpdate(boolean active) {}

  public static void endUpdate() {}

  public static String getKeyText(Keys key, boolean shift/* = false*/) {}
}
