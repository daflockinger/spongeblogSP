package com.flockinger.spongeblogSP.config.httpsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.flockinger.spongeblogSP.service.UserService;


/**
 * Implementation of simple HTTP Auth (user/password).
 *
 */
@Profile({"test","default"})
@Configuration
@EnableWebSecurity
public class SimpleSecurityConfig extends WebSecurityConfigurerAdapter {
 
    private static String REALM="MY_TEST_REALM";
    
    @Autowired
    private UserService userService;
     
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userService);
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
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
        .antMatchers("/swagger-ui.html").hasAuthority("ADMIN")
        .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
     
    @Bean
    public AuthenticationEntryPoint getBasicAuthEntryPoint(){
    	BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
    	entryPoint.setRealmName(REALM);
        return entryPoint;
    }
     
    // To allow Pre-flight [OPTIONS] request from browser 
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}