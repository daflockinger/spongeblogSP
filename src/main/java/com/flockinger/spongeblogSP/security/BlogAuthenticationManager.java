package com.flockinger.spongeblogSP.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flockinger.spongeblogSP.exception.InvalidTokenException;
import com.flockinger.spongeblogSP.service.UserService;

@Service
public class BlogAuthenticationManager implements AuthenticationManager {

  @Autowired
  private UserService userService;
  
  private RestTemplate restTemplate = new RestTemplate();
  
  @Value("${google.verificationUrl}")
  private String verificationUrl;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    BlogAuthentication wrapper = new BlogAuthentication(authentication);
    String userLogin = null;
    if(authentication.getPrincipal() != null) {
      userLogin = authentication.getPrincipal().toString();
    }

    try {
      UserDetails details = userService.loadUserByUsername(userLogin);
      wrapper.setAuthorities(details.getAuthorities());
      wrapper.setAuthenticated(true);
      verifyToken(authentication.getCredentials().toString());
    } catch (UsernameNotFoundException e) {
      throw new OAuth2Exception("User tried to login with: " + userLogin, e);
    } catch (InvalidTokenException e) {
      throw new OAuth2Exception("Invalid token!", e);
    } 
    return wrapper;
  }
  
  private void verifyToken(String token) throws InvalidTokenException{
    ResponseEntity<String> response = restTemplate.getForEntity(verificationUrl + token, String.class);
    if( !HttpStatus.OK.equals(response.getStatusCode()) ) {
      throw new InvalidTokenException(response.getBody());
    }
  }
}
