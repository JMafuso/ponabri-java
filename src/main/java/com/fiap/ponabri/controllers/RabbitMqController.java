package com.fiap.ponabri.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String QUEUE_NAME = "simpleQueue";

    @PostMapping("/api/send-message")
    public ResponseEntity<String> sendMessage() {
        String message = "Mensagem de teste para RabbitMQ";
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        return ResponseEntity.ok("Mensagem enviada para a fila RabbitMQ: " + message);
    }
}