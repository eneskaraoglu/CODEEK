package com.screenengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Temporary controller for generating password hashes.
 * REMOVE IN PRODUCTION!
 */
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class PasswordTestController {

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/hash")
    public Map<String, String> generateHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        Map<String, String> result = new HashMap<>();
        result.put("password", password);
        result.put("hash", hash);
        result.put("verify", String.valueOf(passwordEncoder.matches(password, hash)));
        return result;
    }
}
