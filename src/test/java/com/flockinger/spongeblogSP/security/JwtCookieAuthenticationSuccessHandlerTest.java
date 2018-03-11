package com.flockinger.spongeblogSP.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.security.core.Authentication;

import com.flockinger.spongeblogSP.exception.InvalidAuthSuccessRedirectUrlException;


public class JwtCookieAuthenticationSuccessHandlerTest {
  
  private final String fakeTargetUrl = "https://fake.xyz";  
  private final String someToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
      + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik5vdCBtZSIsImlhdCI6MTUxNjIzOTAyMn0."
      + "aQdR0Zqw8rJKiyve79SvKnbOXKCv0SlumOufDfQ5hKU";
  
  @Test
  public void testOnAuthenticationSuccess_withCorrectCredentials_shouldSetCorrectDefaultTargetUrl() throws IOException, ServletException {
    JwtCookieAuthenticationSuccessHandler handler = new JwtCookieAuthenticationSuccessHandler(fakeTargetUrl);
    
    Authentication auth = mock(Authentication.class);
    when(auth.getCredentials()).thenReturn(someToken);
    
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class); 
    handler.onAuthenticationSuccess(mockRequest, mockResponse, auth);
    
    final String expectedTargetUrl = fakeTargetUrl + "?token=" + someToken;
    assertEquals("verify correct default target URL", expectedTargetUrl, Whitebox.getInternalState(handler, "defaultTargetUrl").toString());
  }

  
  @Test(expected=InvalidAuthSuccessRedirectUrlException.class)
  public void testOnAuthenticationSuccess_withNullAuthentication_shouldThrowException() throws IOException, ServletException {
    JwtCookieAuthenticationSuccessHandler handler = new JwtCookieAuthenticationSuccessHandler(fakeTargetUrl);
        
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class); 
    handler.onAuthenticationSuccess(mockRequest, mockResponse, null);
  }
  
  @Test(expected=InvalidAuthSuccessRedirectUrlException.class)
  public void testOnAuthenticationSuccess_withNullCredentialsToken_shouldThrowException() throws IOException, ServletException {
    JwtCookieAuthenticationSuccessHandler handler = new JwtCookieAuthenticationSuccessHandler(fakeTargetUrl);
    
    Authentication auth = mock(Authentication.class);
    when(auth.getCredentials()).thenReturn(null);
    
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    HttpServletResponse mockResponse = mock(HttpServletResponse.class); 
    handler.onAuthenticationSuccess(mockRequest, mockResponse, auth);
  }
}
