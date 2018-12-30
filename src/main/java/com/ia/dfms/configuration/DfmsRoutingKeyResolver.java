package com.ia.dfms.configuration;

import org.apache.commons.lang3.StringUtils;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.amqp.eventhandling.RoutingKeyResolver;
import org.springframework.stereotype.Component;

import com.ia.dfms.enums.DefaultAMQProperties;
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
        return isValid(customRoutingKey) ? customRoutingKey : DefaultAMQProperties.DFMS_EVENTS.getRoutingKey();
    }

    private Boolean isValid(String key) {
        log.info("Custom routing key found. value: {}", key);
        return !(StringUtils.isEmpty(key) || StringUtils.isBlank(key));
    }
}
