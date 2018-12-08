package com.ia.dfms.util;

import java.util.Optional;

import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.GenericMessage;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AggregateUtil {
 
    private final EventStore store;

    public <T> Optional<T> aggregateGet(String aggregateId, Class<T> clazz) {
        final UnitOfWork<GenericMessage<String>> uow = DefaultUnitOfWork.startAndGet(new GenericMessage<>(aggregateId));
        final Repository<T> repository = new EventSourcingRepository<T>(clazz, store);
        try {            
            final Aggregate<T> aggreagte = repository.load(aggregateId);
            return Optional.of(aggreagte.invoke(a -> {
                uow.commit();
                return a;
            }));
        } catch (AggregateNotFoundException e) {
            uow.rollback();
            return Optional.empty();
        }
    }
}
