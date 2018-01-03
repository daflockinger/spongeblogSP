package com.flockinger.spongeblogSP.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class HeaderTokenAuthFilterTest {
  
  private HeaderTokenAuthFilter filter;
  private JwtTokenUtils utils = new JwtTokenUtils();
  
  @Test
  public void testAttemptAuthentication_withValidToken_shouldSucceed() throws AuthenticationException, IOException, ServletException {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlNlcHAgbWFpYSIsImVtYWlsIjoiZmxvQG1lLmNjIn0."
        + "uTgBCnbIndf5ChZxANCQWvXT6h7eATngR1iEuq5t5gs";
    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    HttpServletRequest fakeRequest = mock(HttpServletRequest.class);
    when(fakeRequest.getHeader(anyString())).thenReturn("Bearer " + token);
    when(mockManager.authenticate(any())).thenReturn(null);
    filter = new HeaderTokenAuthFilter("http://localhost/nothing", mockManager, utils);

    filter.attemptAuthentication(fakeRequest, null);
    
    ArgumentCaptor<Authentication> captureAuth = ArgumentCaptor.forClass(Authentication.class);
    verify(mockManager).authenticate(captureAuth.capture());
    Authentication resultAuth = captureAuth.getValue();
    assertNotNull("verify returned Authentication is not null", resultAuth);
    assertTrue("verify that user it authenticated", resultAuth.isAuthenticated());
    assertEquals("verify that the principal is correct", "flo@me.cc", resultAuth.getPrincipal());
    assertEquals("verify authentication contains correct token", token, resultAuth.getCredentials());
  }
  
  @Test(expected=BadCredentialsException.class)
  public void testAttemptAuthentication_withTokenWithoutEmail_shouldThrowException() throws AuthenticationException, IOException, ServletException {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlNlcHAgbWFpYSIsInNvbWV0aGluZyI6IndoYXQncyB1cD8ifQ"
        + ".GCLfCQ3VAmgBYTfbTZ8U-xcsZ9dGaA1xWHJf5sY8S7A";
    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    HttpServletRequest fakeRequest = mock(HttpServletRequest.class);
    when(fakeRequest.getHeader(anyString())).thenReturn("Bearer " + token);
    when(mockManager.authenticate(any())).thenReturn(null);
    filter = new HeaderTokenAuthFilter("http://localhost/nothing", mockManager, utils);

    filter.attemptAuthentication(fakeRequest, null);
  }
  
  @Test(expected=BadCredentialsException.class)
  public void testAttemptAuthentication_withFakeToken_shouldThrowException() throws AuthenticationException, IOException, ServletException {
    String token = "fakeToken";
    AuthenticationManager mockManager = mock(AuthenticationManager.class);
    HttpServletRequest fakeRequest = mock(HttpServletRequest.class);
    when(fakeRequest.getHeader(anyString())).thenReturn("Bearer " + token);
    when(mockManager.authenticate(any())).thenReturn(null);
    filter = new HeaderTokenAuthFilter("http://localhost/nothing", mockManager, utils);

    filter.attemptAuthentication(fakeRequest, null);
  }
}
