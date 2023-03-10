package com.ntnn.config;

import com.ntnn.config.strategy.CloudParameterStore;
import com.ntnn.config.strategy.Params;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.util.List;

@Log4j2
@Configuration
public class RabbitMqConfiguration {

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private CloudParameterStore cloudParameterStore;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        List<Params.UserMq> list = cloudParameterStore.authorMq().getMq();
        connectionFactory.setUsername(list.get(0).getUsername());
        connectionFactory.setPassword(list.get(0).getPassword());
        connectionFactory.setHost(list.get(0).getHostname());
        connectionFactory.setPort(Integer.parseInt(rabbitMqProperties.getPort()));
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setReceiveTimeout((long) rabbitMqProperties.getReceiveTimeOut());
        factory.setConcurrentConsumers(rabbitMqProperties.getConcurrentMin());
        factory.setMaxConcurrentConsumers(rabbitMqProperties.getConcurrentMax());
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setErrorHandler(errorHandler());
        if (rabbitMqProperties.isAutoAck()) {
            factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        } else {
            factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        }

        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
    }
    public static class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
        @Override
        public boolean isFatal(Throwable t) {
            if (t instanceof ListenerExecutionFailedException) {
                ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
                log.error("Failed to process inbound message from queue {}; failed message: {} ",
                        lefe.getFailedMessage().getMessageProperties().getConsumerQueue(),
                        lefe.getFailedMessage(), t);
            }
            return super.isFatal(t);
        }
    }

    @Bean("queueAccount")
    public Queue queueAccount() {
        return new Queue(rabbitMqProperties.getQueues().get(0), true, false, false);
    }

    @Bean("queuePayment")
    public Queue queuePayment() {
        return new Queue(rabbitMqProperties.getQueues().get(1), true, false, false);
    }
}
