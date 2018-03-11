package com.flockinger.spongeblogSP.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.flockinger.spongeblogSP.exception.InvalidAuthSuccessRedirectUrlException;

/**
 * Adds Token to query string and removes possible other URL query parameters.
 *
 */
public class JwtCookieAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final static String TOKEN_NAME = "token";
  
  public JwtCookieAuthenticationSuccessHandler(String finalTargetUrl) {
    super(finalTargetUrl);
  }
  
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String targetUrl = getDefaultTargetUrl();
    assertAuthSuccessParameters(authentication);
    String tokenQuery = "?" + TOKEN_NAME + "=" + authentication.getCredentials();
    setDefaultTargetUrl(StringUtils.substringBeforeLast(targetUrl, "?") + tokenQuery);
        
    super.onAuthenticationSuccess(request, response, authentication);
  }
  
  private void assertAuthSuccessParameters(Authentication authentication) throws InvalidAuthSuccessRedirectUrlException {
    if(authentication == null) {
      throw new InvalidAuthSuccessRedirectUrlException("Authentication cannot be null!");
    }
    if(authentication != null && authentication.getCredentials() == null) {
      throw new InvalidAuthSuccessRedirectUrlException("Credentials must contain valid token and cannot be empty/null!");
    }
  }
}