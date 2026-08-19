package com.insightzz.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserTestController {

    @GetMapping("/health")
    public Map<String, Object> health() {

        return Map.of(
                "service", "user-service",
                "status", "UP",
                "message", "User service is running"
        );
    }
}
