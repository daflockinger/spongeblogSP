package com.flockinger.spongeblogSP.security;

import java.security.Principal;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("default")
@Configuration
@EnableOAuth2Client
@RestController
public class SecurityConfig {

  @RequestMapping("/user")
  public Principal user(Principal principal) {
    return principal;
  }
}
