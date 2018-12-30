package com.ia.dfms.configuration.publisher;

import org.apache.commons.lang3.StringUtils;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.amqp.eventhandling.RoutingKeyResolver;
import org.springframework.stereotype.Component;

import com.ia.dfms.events.AbtractEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DfmsRoutingKeyResolver implements RoutingKeyResolver {

    @Override
    public String resolveRoutingKey(EventMessage<?> event) {
        log.info("Resolving Routing Key");
        final AbtractEvent myEvent = (AbtractEvent) event.getPayload();
        final String customRoutingKey = myEvent.getRoutingKey();
        if (StringUtils.isEmpty(customRoutingKey) || StringUtils.isBlank(customRoutingKey)) {
            log.info("no custom routing key found a default routing key will be used.");
            return event.getPayloadType().getPackage().getName();
        } else {
            log.info("Custom routing key found. value: {}", customRoutingKey);
            return customRoutingKey;
        }
    }
}
