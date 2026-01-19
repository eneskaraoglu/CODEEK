package com.screenengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Screen Engine - Universal Dynamic Screen Platform
 *
 * Main Spring Boot application class for the Screen Engine backend.
 * This application provides a metadata-driven platform for creating
 * dynamic data entry and query screens without writing code.
 *
 * Key Features:
 * - Metadata-driven UI generation
 * - Database-agnostic SQL query building
 * - Multi-tenant support
 * - Role-based access control
 * - RESTful API
 *
 * @version 0.1.0
 * @since 2025-01-19
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class ScreenEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScreenEngineApplication.class, args);
    }

}
