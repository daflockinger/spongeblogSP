package com.flockinger.spongeblogSP.config;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class GeneralConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
  
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
