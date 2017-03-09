package com.flockinger.spongeblogSP.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages="com.flockinger.spongeblogSP.dao")
@EnableTransactionManagement
public class JPAConfig {

}
