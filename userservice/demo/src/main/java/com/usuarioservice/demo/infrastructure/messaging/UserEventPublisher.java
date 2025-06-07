package com.usuarioservice.demo.infrastructure.messaging;

import com.usuarioservice.demo.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishUserCreated(Object user) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_CREATED_QUEUE, user);
    }
}
