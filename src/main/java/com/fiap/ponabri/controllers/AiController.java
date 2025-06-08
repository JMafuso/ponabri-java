package com.fiap.ponabri.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api")
public class AiController {

    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    public AiController() {
        // Constructor without ChatClient since dependency is commented out
    }

    @PostConstruct
    public void verifyAiClient() {
        logger.info("Spring AI ChatClient is disabled, skipping verification.");
    }

    @GetMapping("/ask-ai")
    public ResponseEntity<String> askAi(@RequestParam(value = "question") String question) {
        String resposta;

        switch (question.toLowerCase().trim()) {
            case "o que fazer em uma enchente?":
                resposta = "Em caso de enchente, suba para lugares altos, desligue a energia e o gás, e evite contato com a água. Procure um abrigo seguro se a sua casa estiver em risco.";
                break;
            case "o que deve fazer em um terremoto":
                resposta = "Durante um terremoto, procure se abrigar debaixo de móveis resistentes ou em um canto da parede. Mantenha distância de janelas e objetos que possam cair. Após o tremor, vá para um local aberto e seguro.";
                break;
            case "por que devo escolher a ponabri para me abrigar?":
                resposta = "A Ponabri conecta você aos abrigos comunitários mais próximos e seguros em tempo real durante eventos extremos. Garantimos informações atualizadas sobre vagas, recursos e segurança, protegendo sua família em momentos críticos.";
                break;
            case "a ponabri é confiavel?":
                resposta = "Sim, a Ponabri busca oferecer informações em tempo real e confiáveis sobre abrigos comunitários, trabalhando em parceria com a defesa civil e voluntários para garantir a segurança da população em eventos extremos.";
                break;
            default:
                resposta = "Olá, sou a IA do Ponabri. Para mais informações, por favor, me pergunte sobre enchentes, terremotos ou a confiabilidade da Ponabri.";
                break;
        }

        logger.info("Pergunta: {}", question);
        logger.info("Resposta da IA (hardcoded): {}", resposta);
        return ResponseEntity.ok(resposta);
    }
}
