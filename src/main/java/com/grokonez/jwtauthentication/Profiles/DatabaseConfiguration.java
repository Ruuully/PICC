package com.grokonez.jwtauthentication.Profiles;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
public class DatabaseConfiguration {

    private String username;
    private String password;
    private String url;

    @Profile("dev")
    @Bean
    public String devProfil() {
        return "dev";
    }

    @Profile("test")
    @Bean
    public String testProfil() {
        return "test";
    }

    @Profile("prod")
    @Bean
    public String prodProfil() {
        return "prod";
    }
}
