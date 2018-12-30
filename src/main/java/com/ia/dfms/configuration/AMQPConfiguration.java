package com.ia.dfms.configuration;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.RoutingKeyResolver;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ia.dfms.configuration.publisher.Publisher;
import com.ia.dfms.enums.DefaultAMQProperties;

@Configuration
public class AMQPConfiguration {

    @Value("${axon.amqp.exchange}")
    private String exchange;

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(exchange).build();
    }

    @Bean
    public Queue defaultEventsQueue() {
        return QueueBuilder.durable(DefaultAMQProperties.DFMS_EVENTS.getQueue()).build();
    }

    @Bean
    public Binding defaultBinding() {
        return BindingBuilder.bind(defaultEventsQueue()).to(exchange()).with(DefaultAMQProperties.DFMS_EVENTS.getRoutingKey()).noargs();
    }

    @Bean
    public AMQPMessageConverter amqpMessageConverter(Serializer serializer, RoutingKeyResolver resolver) {
        return DefaultAMQPMessageConverter.builder().serializer(serializer).routingKeyResolver(resolver).build();
    }

    @Bean
    public Publisher publisher(AMQPMessageConverter messageConverter, EventBus bus, Exchange exchange) {
        final Publisher publisher = new Publisher(bus);
        publisher.setExchange(exchange);
        publisher.setMessageConverter(messageConverter);
        return publisher;
    }

    @Autowired
    public void configure(AmqpAdmin admin) {
        admin.declareExchange(exchange());
        admin.declareQueue(defaultEventsQueue());
        admin.declareBinding(defaultBinding());
    }

    @Autowired
    public void setInterceptor(EventBus bus, MessageDispatchInterceptor<EventMessage<?>> interceptor) {
        bus.registerDispatchInterceptor(interceptor);
    }
}
