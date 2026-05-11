package org.mcsr.aatool;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Main {
  public static void main(String[] args) {
    String greeting = "Hello World!";
    JFrame frame = new JFrame(greeting);
    frame.setSize(500, 500);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.getContentPane().add(new JLabel(greeting, SwingConstants.CENTER));
    frame.setVisible(true);
  }
}
