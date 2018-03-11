package com.flockinger.spongeblogSP.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.RestTemplate;

import com.flockinger.spongeblogSP.dto.BlogUserDetails;
import com.flockinger.spongeblogSP.service.BaseServiceTest;
import com.flockinger.spongeblogSP.service.UserService;
import com.google.common.collect.ImmutableList;

public class BlogAuthenticationManagerTest extends BaseServiceTest {
  
  @MockBean
  private UserService userService;
  @MockBean
  private RestTemplate restTemplate;
  
  @Autowired
  private BlogAuthenticationManager authManager;
  
  @Test
  public void testAuthenticate_withValidPrinciple_shouldReturnCorrect() {
    String userLogin = "existingUser";
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9."
        + "TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
    
    BlogUserDetails details = new BlogUserDetails();
    details.setEnabled(true);
    details.setAuthorities(ImmutableList.of(new SimpleGrantedAuthority("ADMIN")));
    when(userService.loadUserByUsername(matches(userLogin))).thenReturn(details);
    when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    
    Authentication result = authManager.authenticate(getFakeAuth(userLogin, token));
    assertNotNull("verify returned Authentication is not null", result);
    assertTrue("verify that user it authenticated", result.isAuthenticated());
    assertFalse("verify that it contains authorities", result.getAuthorities().isEmpty());
    assertEquals("verify that it still contains principal", userLogin, result.getPrincipal());
    assertEquals("verify authentication still contains credentials/token", token, result.getCredentials());
    
    verify(userService).loadUserByUsername(matches(userLogin));
    verify(restTemplate).getForEntity(anyString(), any());
  }
  
  
  @Test(expected=OAuth2Exception.class)
  public void testAuthenticate_withInvalidToken_shouldThrowOauthExcpetion() {
    String userLogin = "existingUser";
    String token = "fakeToken";
    
    BlogUserDetails details = new BlogUserDetails();
    details.setEnabled(true);
    details.setAuthorities(ImmutableList.of(new SimpleGrantedAuthority("ADMIN")));
    when(userService.loadUserByUsername(matches(userLogin))).thenReturn(details);
    when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    
    authManager.authenticate(getFakeAuth(userLogin, token));
  }
  
  @Test(expected=OAuth2Exception.class)
  public void testAuthenticate_withNotExistingUser_shouldThrowOauthExcpetion() {
    String userLogin = "hackerUser";
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9"
        + ".TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
    
    BlogUserDetails details = new BlogUserDetails();
    details.setEnabled(true);
    details.setAuthorities(ImmutableList.of(new SimpleGrantedAuthority("ADMIN")));
    when(userService.loadUserByUsername(matches(userLogin))).thenThrow(UsernameNotFoundException.class);
    when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    
    authManager.authenticate(getFakeAuth(userLogin, token));
  }
  
  
  
  private Authentication getFakeAuth(String principal, String credentials) {
    return new Authentication() {
      @Override
      public String getName() {
        return null;
      }
      
      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
      
      @Override
      public boolean isAuthenticated() {
        return false;
      }
      
      @Override
      public Object getPrincipal() {
        return principal;
      }
      
      @Override
      public Object getDetails() {
        return null;
      }
      
      @Override
      public Object getCredentials() {
        return credentials;
      }
      
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }
    };
  }
}
