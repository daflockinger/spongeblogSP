package com.flockinger.spongeblogSP.exception;

import javax.servlet.ServletException;

public class InvalidAuthSuccessRedirectUrlException extends ServletException {
  /**
   * 
   */
  private static final long serialVersionUID = 2387561171048099819L;

  public InvalidAuthSuccessRedirectUrlException(String message) {
    super(message);
  }
}
