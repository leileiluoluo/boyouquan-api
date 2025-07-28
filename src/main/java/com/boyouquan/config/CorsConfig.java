package com.boyouquan.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;

    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        if (boYouQuanConfig.getCorsOpenWhiteList()) {
            registry.addMapping("/**")
                    .allowedOrigins(boYouQuanConfig.getCorsWhiteList().toArray(new String[]{}))
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowCredentials(true);
        }
    }

}