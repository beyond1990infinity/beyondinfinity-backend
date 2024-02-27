package com.beyond.infinity.demo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {

    @Bean
    @ConfigurationProperties(prefix="jira")
    public JiraConfig gitHub(){
        return new JiraConfig();

    }
}
