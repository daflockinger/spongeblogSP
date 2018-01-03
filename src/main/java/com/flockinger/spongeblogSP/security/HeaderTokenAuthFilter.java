package com.flockinger.spongeblogSP.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.jayway.jsonpath.PathNotFoundException;

public class HeaderTokenAuthFilter extends AbstractAuthenticationProcessingFilter {

  private final static String AUTHENTICATION_HEADER_NAME = "Authorization";
  private JwtTokenUtils tokenUtils;
  
  public HeaderTokenAuthFilter(String defaultFilterProcessesUrl,
      AuthenticationManager authenticationManager, JwtTokenUtils tokenUtils) {
    super(defaultFilterProcessesUrl);
    setAuthenticationManager(authenticationManager);
    this.tokenUtils = tokenUtils;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String authenticationHeader = request.getHeader(AUTHENTICATION_HEADER_NAME);
    
    if (authentication == null && StringUtils.isNotEmpty(authenticationHeader)) {
      authentication = getAuthByRequestHeader(authenticationHeader);
    }
    return authentication;
  }

  private Authentication getAuthByRequestHeader(String authenticationHeader) {
    authenticationHeader = StringUtils.substringAfter(authenticationHeader, " ");
    
    try {
      return getAuthenticationManager().authenticate(tokenUtils.createAuthentiaction(authenticationHeader));
    }  catch (final InvalidTokenException e) {
      throw new BadCredentialsException("Could not obtain user details from token", e);
    } catch (final PathNotFoundException e) {
      throw new BadCredentialsException("Token is missing the identifying email field", e);
    }
  }
  
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    Authentication authResult;

    try {
      authResult = attemptAuthentication(request, response);
      if (authResult != null) {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }
      }
    } catch (InternalAuthenticationServiceException failed) {
      logger.error("An internal error occurred while trying to authenticate the user.", failed);
      unsuccessfulAuthentication(request, response, failed);
      return;
    } catch (AuthenticationException failed) {
      // Authentication failed
      unsuccessfulAuthentication(request, response, failed);
      return;
    }

    chain.doFilter(request, response);
  }

}
