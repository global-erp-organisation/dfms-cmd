package com.ia.dfms.configuration;

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

@Configuration
public class AMQPConfiguration {
    @Value("${external.amqp.queue}")
    private String queueName;

    @Value("${external.amqp.bind.route-key}")
    private String routeKey;

    @Value("${axon.amqp.exchange}")
    private String exchange;

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange(exchange).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("'*'").noargs();
    }

    @Autowired
    public void configure(AmqpAdmin admin) {
        admin.declareExchange(exchange());
        admin.declareQueue(queue());
        admin.declareBinding(binding());
    }
}
