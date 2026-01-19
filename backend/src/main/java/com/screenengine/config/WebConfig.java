package com.screenengine.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for Screen Engine.
 * Configures CORS settings for frontend communication.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ScreenEngineProperties properties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        ScreenEngineProperties.Cors cors = properties.getSecurity().getCors();

        registry.addMapping("/api/**")
                .allowedOrigins(cors.getAllowedOrigins().split(","))
                .allowedMethods(cors.getAllowedMethods().split(","))
                .allowedHeaders(cors.getAllowedHeaders().split(","))
                .allowCredentials(cors.isAllowCredentials())
                .maxAge(3600);
    }
}
