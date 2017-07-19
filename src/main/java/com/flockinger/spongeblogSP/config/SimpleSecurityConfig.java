package com.flockinger.spongeblogSP.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;



/**
 * Implementation of simple HTTP Auth used for service to service communication
 *
 */
@Profile({"test", "default"})
@Configuration
@EnableWebSecurity
public class SimpleSecurityConfig extends WebSecurityConfigurerAdapter {

  private static String REALM = "SPONGE_REALM";

  @Value("${security-admin.clientid}")
  private String adminClientId;
  @Value("${security-admin.secretkey}")
  private String adminSecretkey;

  @Value("${security-author.clientid}")
  private String authorClientId;
  @Value("${security-author.secretkey}")
  private String authorSecretkey;

  @Autowired
  public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(adminClientId).password(adminSecretkey)
        .authorities("ADMIN", "AUTHOR").and().withUser(authorClientId).password(authorSecretkey)
        .authorities("AUTHOR");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/info/**")
        .permitAll().antMatchers(HttpMethod.GET, "/api/v1/users/*").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/api/v1/posts/author/**").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.POST, "/api/v1/posts").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.PUT, "/api/v1/posts").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.DELETE, "/api/v1/posts/*").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.PUT, "/api/v1/posts/rewind/*").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.POST, "/api/v1/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.PUT, "/api/v1/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/api/v1/**").hasAuthority("ADMIN").antMatchers("/")
        .hasAuthority("ADMIN").antMatchers("/swagger-ui.html").hasAuthority("ADMIN").and()
        .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint()).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  public AuthenticationEntryPoint getBasicAuthEntryPoint() {
    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
    entryPoint.setRealmName(REALM);
    return entryPoint;
  }
}
