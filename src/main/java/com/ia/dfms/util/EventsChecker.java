package com.ia.dfms.util;

import java.util.Optional;

import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class EventsChecker<T> {
    private final EventStore store;

    @SuppressWarnings("unchecked")
    public Optional<T> latestEvent(String aggregateId) {
        final DomainEventStream events = store.readEvents(aggregateId);
        return !events.hasNext() ? Optional.<T>empty() : Optional.of((T) events
                .filter(e -> events.getLastSequenceNumber() == null || e.getSequenceNumber() == events.getLastSequenceNumber()).peek().getPayload());
    }
}
