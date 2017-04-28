package com.flockinger.spongeblogSP.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Profile("production")
@Configuration
@EnableWebSecurity
public class BlogSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	private OpenIdFilter filter;
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilterAfter(new OAuth2ClientContextFilter(), 
          AbstractPreAuthenticatedProcessingFilter.class)
        .addFilterAfter(filter, 
          OAuth2ClientContextFilter.class)
        .httpBasic()
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/google-login"))
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/v1/users/info/**").permitAll()
        .antMatchers(HttpMethod.GET, "/api/v1/users/*").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/api/v1/posts/author/**").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.POST, "/api/v1/posts").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.PUT, "/api/v1/posts").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.DELETE, "/api/v1/posts/*").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.PUT, "/api/v1/posts/rewind/*").hasAnyAuthority("ADMIN", "AUTHOR")
        .antMatchers(HttpMethod.POST, "/api/v1/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.PUT, "/api/v1/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/api/v1/**").hasAuthority("ADMIN")
        .antMatchers("/").hasAuthority("ADMIN")
        .antMatchers("/swagger-ui.html").hasAuthority("ADMIN");
    }
}
