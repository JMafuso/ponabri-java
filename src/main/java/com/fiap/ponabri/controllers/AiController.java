package com.fiap.ponabri.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AiController {

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Hello from AI!") String message) {
        // Placeholder implementation since Spring AI dependency is removed
        String response = "AI response placeholder for message: " + message;
        return Map.of("generation", response);
    }
}
