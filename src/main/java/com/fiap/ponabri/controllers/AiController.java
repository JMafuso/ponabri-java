package com.fiap.ponabri.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class AiController {

    @GetMapping("/ask-ai")
    public ResponseEntity<String> askAi(@RequestParam(value = "question", defaultValue = "Olá, sou a IA do Ponabri.") String question) {
        // Resposta fixa para demonstração, pois a integração real com Spring AI não está configurada
        String resposta = "Olá, sou a IA do Ponabri.";
        return ResponseEntity.ok(resposta);
    }
}
