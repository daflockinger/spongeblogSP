package com.flockinger.spongeblogSP.dto;

import org.springframework.security.core.GrantedAuthority;

public class BlogAuthority implements GrantedAuthority {
  /**
   * 
   */
  private static final long serialVersionUID = -2460309696321445650L;

  private String authority;

  public BlogAuthority() {}

  public BlogAuthority(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }
}
