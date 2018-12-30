package com.ia.dfms.configuration;

import org.axonframework.extensions.amqp.AMQPProperties;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.RoutingKeyResolver;
import org.axonframework.extensions.amqp.eventhandling.legacy.JavaSerializationAMQPMessageConverter;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ia.dfms.enums.DefaultAMQProperties;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class AMQPConfiguration {
    
    private AMQPProperties properties;

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(properties.getExchange()).build();
    }

    @Bean
    public Queue defaultEventsQueue() {
        return QueueBuilder.durable(DefaultAMQProperties.DFMS_EVENTS.getQueue()).build();
    }

    @Bean
    public Binding defaultBinding() {
        return BindingBuilder.bind(defaultEventsQueue()).to(exchange()).with(DefaultAMQProperties.DFMS_EVENTS.getRoutingKey()).noargs();
    }

    @Autowired
    public void configure(AmqpAdmin admin) {
        admin.declareExchange(exchange());
        admin.declareQueue(defaultEventsQueue());
        admin.declareBinding(defaultBinding());
    }
    
    @Bean
    public AMQPMessageConverter amqpMessageConverter(Serializer serializer, RoutingKeyResolver resolver) {
        return new JavaSerializationAMQPMessageConverter(serializer, resolver, true);
    }

}
