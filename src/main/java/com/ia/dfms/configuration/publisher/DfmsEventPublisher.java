package com.ia.dfms.configuration.publisher;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class DfmsEventPublisher  implements MessageDispatchInterceptor<EventMessage<?>> {

    private final Publisher publisher;

    @Override
    public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(List<? extends EventMessage<?>> messages) {
        return (index, event) -> {
            log.info("Publishing event: [{}]. event index = {}", event, index);
            publisher.send(Arrays.asList(event));
            log.info("event succesfully publish on the bus.");
            return event;
        };
    }
}
