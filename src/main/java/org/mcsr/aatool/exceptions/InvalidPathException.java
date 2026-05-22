package org.mcsr.aatool.exceptions;

public class InvalidPathException extends IllegalArgumentException {
  public InvalidPathException() { super("Illegal character(s) in custom save path"); }
}
