package com.flockinger.spongeblogSP.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

  @Value("${blog.security.allowed-origin}")
  private String allowedOrigin;
  
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    super.addViewControllers(registry);

    registry.addRedirectViewController("/", "/swagger-ui.html");
  }
  
  @Override
  public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
      .allowedOrigins(allowedOrigin)
      .allowedMethods("*")
      .allowedHeaders("*")
      .maxAge(3600);
      
      // TODO extend
      /*registry.addMapping("/api/**")
        .allowedOrigins("http://domain2.com")
        .allowedMethods("PUT", "DELETE")
            .allowedHeaders("header1", "header2", "header3")
        .exposedHeaders("header1", "header2")
        .allowCredentials(false).maxAge(3600);*/
  }
  
  /*
   @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://domain1.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
   * */
}
