package com.reservaservice.demo.infrastructure.messaging;

import com.reservaservice.demo.application.service.ReservaUserService;
import com.reservaservice.demo.infrastructure.messaging.dto.UserEventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    private ReservaUserService reservaUserService;

    @RabbitListener(queuesToDeclare = @Queue(name = "user.created.queue", durable = "true"))
    public void handleUserCreated(UserEventDto userDto) {
        try {
            System.out.println("Recebendo usuário: " + userDto.getId());
            reservaUserService.createLocalUser(userDto);
            System.out.println("Usuário processado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao processar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }
}