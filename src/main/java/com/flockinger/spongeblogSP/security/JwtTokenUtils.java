package com.flockinger.spongeblogSP.security;

import java.util.ArrayList;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Component
public class JwtTokenUtils {
  
  public String extractIdToken(OAuth2AccessToken accessToken) throws InvalidTokenException {
    final Object idToken = accessToken.getAdditionalInformation().get("id_token");
    if(idToken == null) {
      throw new InvalidTokenException("Invalid (null) token received!");
    }
    return idToken.toString();
  }
  
  public Authentication createAuthentiaction(String idToken) {
    return new UsernamePasswordAuthenticationToken(
        getEmailFromToken(idToken), idToken, new ArrayList<>());
  }
  
  private String getEmailFromToken(String idToken) throws BadCredentialsException {
    final Jwt tokenDecoded;
    try {
      tokenDecoded = JwtHelper.decode(idToken);
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException("This is not a JWT token: " + e.getMessage(), e);
    }
    DocumentContext tokenContext = JsonPath.parse(tokenDecoded.getClaims());
    return tokenContext.read("$.email");
  }
}
