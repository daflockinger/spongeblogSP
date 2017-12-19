package com.flockinger.spongeblogSP.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class OpenIdFilter extends AbstractAuthenticationProcessingFilter {
  public OAuth2RestOperations restTemplate;

  public OpenIdFilter(String defaultFilterProcessesUrl,
      AuthenticationManager authenticationManager, String finalTargetUrl) {
    super(defaultFilterProcessesUrl);
    setAuthenticationManager(authenticationManager);
    setAuthenticationSuccessHandler(new JwtCookieAuthenticationSuccessHandler(finalTargetUrl));
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
      final String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
      final Jwt tokenDecoded = JwtHelper.decode(idToken);
      DocumentContext tokenContext = JsonPath.parse(tokenDecoded.getClaims());
      return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
          tokenContext.read("$.email"), idToken, new ArrayList<>()));
    } catch (final InvalidTokenException e) {
      throw new BadCredentialsException("Could not obtain user details from token", e);
    }
  }

  public void setRestTemplate(OAuth2RestTemplate restTemplate2) {
    restTemplate = restTemplate2;
  }

}
