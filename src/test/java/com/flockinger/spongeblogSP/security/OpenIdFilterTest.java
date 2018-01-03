package com.flockinger.spongeblogSP.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.google.common.collect.ImmutableMap;

public class OpenIdFilterTest {
  
  private OpenIdFilter filter;
  private JwtTokenUtils tokenUtils = new JwtTokenUtils();
  
  @Test
  public void testAttemptAuthentication_withValidToken_shouldWork() throws AuthenticationException, IOException, ServletException {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlNlcHAgbWFpYSIsImVtYWlsIjoiZmxvQG1lLmNjIn0."
        + "uTgBCnbIndf5ChZxANCQWvXT6h7eATngR1iEuq5t5gs";
    
    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    OAuth2RestTemplate mockTemplate = mock(OAuth2RestTemplate.class);
    OAuth2AccessToken mockToken = mock(OAuth2AccessToken.class);
    filter = new OpenIdFilter("__", mockManager, "http://localhost/bla", tokenUtils);
    filter.setRestTemplate(mockTemplate);
    
    when(mockManager.authenticate(any())).thenReturn(null);
    when(mockTemplate.getAccessToken()).thenReturn(mockToken);
    when(mockToken.getAdditionalInformation()).thenReturn(ImmutableMap.of("id_token", token));
    
    filter.attemptAuthentication(null, null);
    
    ArgumentCaptor<Authentication> captureAuth = ArgumentCaptor.forClass(Authentication.class);
    verify(mockManager).authenticate(captureAuth.capture());
    Authentication resultAuth = captureAuth.getValue();
    assertNotNull("verify returned Authentication is not null", resultAuth);
    assertTrue("verify that user it authenticated", resultAuth.isAuthenticated());
    assertEquals("verify that the principal is correct", "flo@me.cc", resultAuth.getPrincipal());
    assertEquals("verify authentication contains correct token", token, resultAuth.getCredentials());
  }
  
  @Test(expected=BadCredentialsException.class)
  public void testAttemptAuthentication_withTokenWithoutEmail_shouldThrowCorrectException() throws AuthenticationException, IOException, ServletException {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlNlcHAgbWFpYSIsInNvbWV0aGluZyI6IndoYXQncyB1cD8ifQ"
        + ".GCLfCQ3VAmgBYTfbTZ8U-xcsZ9dGaA1xWHJf5sY8S7A";
    
    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    OAuth2RestTemplate mockTemplate = mock(OAuth2RestTemplate.class);
    OAuth2AccessToken mockToken = mock(OAuth2AccessToken.class);
    filter = new OpenIdFilter("__", mockManager, "http://localhost/bla", tokenUtils);
    filter.setRestTemplate(mockTemplate);
    
    when(mockManager.authenticate(any())).thenReturn(null);
    when(mockTemplate.getAccessToken()).thenReturn(mockToken);
    when(mockToken.getAdditionalInformation()).thenReturn(ImmutableMap.of("id_token", token));
    
    filter.attemptAuthentication(null, null);
    
    verify(mockManager).authenticate(any());
  }
  
  @Test(expected=BadCredentialsException.class)
  public void testAttemptAuthentication_withTotalyInvalidToken_shouldThrowCorrectException() throws AuthenticationException, IOException, ServletException {
    String token = "fakeToken";
    
    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    OAuth2RestTemplate mockTemplate = mock(OAuth2RestTemplate.class);
    OAuth2AccessToken mockToken = mock(OAuth2AccessToken.class);
    filter = new OpenIdFilter("__", mockManager, "http://localhost/bla", tokenUtils);
    filter.setRestTemplate(mockTemplate);
    
    when(mockManager.authenticate(any())).thenReturn(null);
    when(mockTemplate.getAccessToken()).thenReturn(mockToken);
    when(mockToken.getAdditionalInformation()).thenReturn(ImmutableMap.of("id_token", token));
    
    filter.attemptAuthentication(null, null);
    
    verify(mockManager).authenticate(any());
  }
}
