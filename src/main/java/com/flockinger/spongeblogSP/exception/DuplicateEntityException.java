package com.flockinger.spongeblogSP.exception;

public class DuplicateEntityException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = -2282793105714562541L;

  public DuplicateEntityException(String message) {
    super(message + " already exists.");
  }
}
