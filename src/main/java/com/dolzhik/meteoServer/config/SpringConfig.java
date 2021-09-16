package com.dolzhik.meteoServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
//@EnableJpaRepositories("com.dolzhik.meteoServer.repository")
public class SpringConfig {
}
