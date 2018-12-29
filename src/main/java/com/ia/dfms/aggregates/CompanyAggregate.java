package com.ia.dfms.aggregates;

import java.util.Collections;
import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.CompanyCreationCmd;
import com.ia.dfms.commands.deletion.CompanyDeletionCmd;
import com.ia.dfms.commands.update.CompanyUpdateCmd;
import com.ia.dfms.enums.DefaultAMQProperties;
import com.ia.dfms.events.creation.CompanyCreatedEvent;
import com.ia.dfms.events.deletion.CompanyDeletedEvent;
import com.ia.dfms.events.update.CompanyUpdatedEvent;
import com.ia.dfms.util.EventBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@ToString
@EqualsAndHashCode
@Slf4j
public class CompanyAggregate {
    @AggregateIdentifier
    private String id;
    private String name;
    private Map<String, Object> details = Collections.emptyMap();

    @CommandHandler
    public CompanyAggregate(CompanyCreationCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.companyCreatedEvent(cmd));
        log.info("The creation of the Company with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onCompanyCreated(CompanyCreatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.details = this.getDetails();
        log.info("Creation event of the Company with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleCompanyDeletion(CompanyDeletionCmd cmd) {
        AggregateLifecycle.apply(CompanyDeletedEvent.builder()
                .id(id)
                .routingKey(DefaultAMQProperties.DFMS_EVENTS.getRoutingKey())
                .build()
                );
        log.info("The deletion of the Company with id [{}] have been successfully executed", cmd.getCompanyId());

    }

    @EventSourcingHandler
    public void onCompanyDeletion(CompanyDeletedEvent event) {
        this.id = event.getId();
        AggregateLifecycle.markDeleted();
        log.info("Deletion event of the Company with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void hanldeCompanyUpdate(CompanyUpdateCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.companyUpdatedEvent(cmd));
        log.info("The update of the Company with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onComanyUpdate(CompanyUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.details = event.getDetails();
        log.info("Update event of the Company with id [{}] have been successfully send to the event bus", event.getId());
    }
}
