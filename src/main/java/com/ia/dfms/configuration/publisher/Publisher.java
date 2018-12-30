package com.ia.dfms.configuration.publisher;

import java.util.List;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPPublisher;

public class Publisher extends SpringAMQPPublisher {
    public Publisher(EventBus bus) {
        super(bus);
    }
    
    @Override
    public void send(List<? extends EventMessage<?>> events) {
        super.send(events);
    }
}
