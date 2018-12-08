package com.ia.dfms.aggregates;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.CompanyCreationCmd;
import com.ia.dfms.commands.deletion.CompanyDeletionCmd;
import com.ia.dfms.commands.update.CompanyUpdateCmd;
import com.ia.dfms.events.creation.CompanyCreatedEvent;
import com.ia.dfms.events.deletion.CompanyDeletedEvent;
import com.ia.dfms.events.update.CompanyUpdatedEvent;
import com.ia.dfms.util.AggregateUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Aggregate
@ToString
@EqualsAndHashCode
@Slf4j
public class CompanyAggregate {
    @AggregateIdentifier
    private String id;
    private String name;
    private Map<String, Object> details = Collections.emptyMap();

    @CommandHandler
    public CompanyAggregate(CompanyCreationCmd cmd) {
        AggregateLifecycle.apply(CompanyCreatedEvent.builder()
                .details(cmd.getDetails())
                .id(cmd.getId())
                .name(cmd.getName())
                .build());
    }

    @EventSourcingHandler
    public void onCompanyCreated(CompanyCreatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.details = this.getDetails();
    }

    @CommandHandler
    public void handleCompanyDeletion(CompanyDeletionCmd cmd) {
        AggregateLifecycle.apply(CompanyDeletedEvent.builder()
                .companyId(cmd.getCompanyId())
                .build());
    }

    @EventSourcingHandler
    public void onCompanyDeletion(CompanyDeletedEvent event) {
        this.id = event.getCompanyId();
        AggregateLifecycle.markDeleted();
    }

    @CommandHandler
    public void hanldeCompanyUpdate(CompanyUpdateCmd cmd, AggregateUtil checker) {
        final Optional<CompanyAggregate> event = checker.aggregateGet(cmd.getId(), CompanyAggregate.class);
        if (event.isPresent()) {
            AggregateLifecycle.apply(CompanyUpdatedEvent.builder()
                    .details(cmd.getDetails())
                    .name(cmd.getName()).id(cmd.getId()).build());
            log.info("The update of the Company with id [{}] have been successfully executed", cmd.getId());
        }
        log.info("The update of the Company with id [{}] have been ignored. cause aggregate not found in the event storage", cmd.getId());
    }

    @EventSourcingHandler
    public void onComanyUpdate(CompanyUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.details = event.getDetails();
    }
}
