package com.flockinger.spongeblogSP.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


import com.jayway.jsonpath.PathNotFoundException;

public class OpenIdFilter extends AbstractAuthenticationProcessingFilter {
  private OAuth2RestOperations restTemplate;
  private JwtTokenUtils utils;
  
  public OpenIdFilter(String defaultFilterProcessesUrl,
      AuthenticationManager authenticationManager, String finalTargetUrl, JwtTokenUtils utils) {
    super(defaultFilterProcessesUrl);
    setAuthenticationManager(authenticationManager);
    setAuthenticationSuccessHandler(new JwtCookieAuthenticationSuccessHandler(finalTargetUrl));
    this.utils = utils;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    OAuth2AccessToken accessToken;
    try {
      accessToken = restTemplate.getAccessToken();
    } catch (final OAuth2Exception e) {
      throw new BadCredentialsException("Could not obtain access token", e);
    }
    try {
      return getAuthenticationManager().authenticate(utils.createAuthentiaction(utils.extractIdToken(accessToken)));
    }  catch (final InvalidTokenException e) {
      throw new BadCredentialsException("Could not obtain user details from token", e);
    } catch (final PathNotFoundException e) {
      throw new BadCredentialsException("Token is missing the identifying email field", e);
    }
  }

  public void setRestTemplate(OAuth2RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
}
