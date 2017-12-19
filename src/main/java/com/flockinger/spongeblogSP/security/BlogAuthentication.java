package com.flockinger.spongeblogSP.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class BlogAuthentication  implements Authentication {

  /**
  * 
  */
  private static final long serialVersionUID = 3399132926949107510L;

  private boolean isAuthenticated = false; 
  private Object principal;
  private Object details;
  private Object credentials;
  private String name;
  private Collection<? extends GrantedAuthority> authorities =
      Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));;

  public BlogAuthentication(Authentication authentication) {
    this.principal = authentication.getPrincipal();
    this.details = authentication.getDetails();
    this.credentials = authentication.getCredentials();
    this.name = authentication.getName();
  }

  public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

  @Override
  public Object getDetails() {
    return details;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.isAuthenticated = isAuthenticated;
  }

}
