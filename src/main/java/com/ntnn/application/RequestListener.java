package com.ntnn.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@DependsOn("rabbitListenerContainerFactory")
public class RequestListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queuePayment;


    @RabbitListener(
            queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue("account"),
            containerFactory = "rabbitListenerContainerFactory")
    private void onMessage(String commonModel) {
        log.info("Queue account want to connect with account='{}'", commonModel);
        rabbitTemplate.convertAndSend(queuePayment.getName(), commonModel);
    }
}
