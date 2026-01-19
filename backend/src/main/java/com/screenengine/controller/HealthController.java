package com.screenengine.controller;

import com.screenengine.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for verifying the application is running.
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("application", "Screen Engine");
        healthInfo.put("version", "0.1.0");
        healthInfo.put("timestamp", LocalDateTime.now());

        return ApiResponse.success(healthInfo, "Application is running");
    }
}
