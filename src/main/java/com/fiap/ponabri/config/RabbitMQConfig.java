package com.fiap.ponabri.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SIMPLE_QUEUE = "simpleQueue";

    @Bean
    public Queue simpleQueue() {
        return new Queue(SIMPLE_QUEUE, true);
    }
}
