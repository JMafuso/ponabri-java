package com.fiap.ponabri.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RabbitMqConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumer.class);

    @RabbitListener(queues = "simpleQueue")
    public void receiveMessage(String message) {
        logger.info("Mensagem recebida da fila simpleQueue: {}", message);
    }
}
