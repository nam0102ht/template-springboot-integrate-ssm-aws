package com.ntnn.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "infrastructure.services.rabbitmq.*")
public class RabbitMqProperties {
    private String port;
    private Integer concurrentMin;
    private Integer concurrentMax;
    private List<String> queues;
    private List<String> topicExchange;
    private boolean autoAck;
    private int receiveTimeOut;
}
