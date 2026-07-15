package org.mcsr.aatool.net;

public interface NetworkController {
  void setControlStates(String buttonText, boolean enableButton, boolean enableDropDown);
  void writeToConsole(String text);
  void syncConsole();
  void syncUserList(Iterable<User> users);
}
