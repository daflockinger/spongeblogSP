package com.flockinger.spongeblogSP.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class HeaderTokenAuthFilter extends AbstractAuthenticationProcessingFilter {

  public HeaderTokenAuthFilter(String defaultFilterProcessesUrl,
      AuthenticationManager authenticationManager) {
    super(defaultFilterProcessesUrl);
    setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Enumeration<String> headers = request.getHeaderNames();
    
    if (authentication == null && StringUtils.isNotEmpty(request.getHeader("Authorization"))) {
      authentication = getAuthByRequestHeader(request);
    }
    return authentication;
  }

  private Authentication getAuthByRequestHeader(HttpServletRequest request) {
    String authValue = request.getHeader("Authorization");

    authValue = StringUtils.substringAfter(authValue, " ");
    final Jwt tokenDecoded = JwtHelper.decode(authValue);
    DocumentContext tokenContext = JsonPath.parse(tokenDecoded.getClaims());

    return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
        tokenContext.read("$.email"), authValue, new ArrayList<>()));
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
