package com.flockinger.spongeblogSP.config;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.flockinger.spongeblogSP.security.HeaderTokenAuthFilter;
import com.flockinger.spongeblogSP.security.JwtTokenUtils;
import com.flockinger.spongeblogSP.security.OpenIdFilter;



/**
 * Implementation of Open ID Connect Auth used for UI to service communication
 *
 */
@Profile({"test", "default"})
@Configuration
@EnableWebSecurity
public class SimpleSecurityConfig extends WebSecurityConfigurerAdapter { 
  
  @Value("${google.client.clientId}")
  private String clientId;

  @Value("${google.client.clientSecret}")
  private String clientSecret;

  @Value("${google.client.accessTokenUri}")
  private String accessTokenUri;

  @Value("${google.client.userAuthorizationUri}")
  private String userAuthorizationUri;

  @Value("${google.finalTargetUrl}")
  private String finalTargetUrl;

  @Autowired
  private OAuth2ClientContext oauth2ClientContext;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtils tokenUtils;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
    .addFilterAfter(new OAuth2ClientContextFilter(),
        AbstractPreAuthenticatedProcessingFilter.class)
        .addFilterAfter(ssoFilter(), OAuth2ClientContextFilter.class)
        .addFilterAfter(new HeaderTokenAuthFilter("__", authenticationManager, tokenUtils), OpenIdFilter.class)
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/v1/users/info/**")
        .permitAll().antMatchers(HttpMethod.GET, "/api/v1/users/*").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.POST, "/api/v1/posts").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.PUT, "/api/v1/posts").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.DELETE, "/api/v1/posts/*").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.PUT, "/api/v1/posts/rewind/*").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.POST, "/api/v1/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.PUT, "/api/v1/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/api/v1/**").hasAuthority("ADMIN").antMatchers("/")
        .hasAuthority("ADMIN").antMatchers("/swagger-ui.html").hasAuthority("ADMIN")
        .and()
        .logout().logoutUrl("/admin/logout").logoutSuccessUrl("/").permitAll().and().cors()
        .and().csrf().disable();
  }
  
  private Filter ssoFilter() {
    OpenIdFilter filter = new OpenIdFilter("/login", authenticationManager, finalTargetUrl, tokenUtils);
    OAuth2RestTemplate template = new OAuth2RestTemplate(google(), oauth2ClientContext);
    filter.setRestTemplate(template);
    return filter;
  }

  @Bean
  public OAuth2ProtectedResourceDetails google() {
    AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
    details.setClientId(clientId);
    details.setClientSecret(clientSecret);
    details.setAccessTokenUri(accessTokenUri);
    details.setUserAuthorizationUri(userAuthorizationUri);
    details.setScope(Arrays.asList("openid", "email"));
    details.setUseCurrentUri(true);
    return details;
  }
}
