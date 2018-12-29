package com.ia.dfms.aggregates;

import java.util.Collections;
import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.ResourceCreationCmd;
import com.ia.dfms.commands.deletion.ResourceDeletionCmd;
import com.ia.dfms.commands.update.ResourceUpdateCmd;
import com.ia.dfms.events.creation.ResourceCreatedEvent;
import com.ia.dfms.events.deletion.ResourceDeletedEvent;
import com.ia.dfms.events.update.ResourceUpdatedEvent;
import com.ia.dfms.util.EventBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@EqualsAndHashCode
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
@Slf4j
public class ResourceAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private String email;
    private String phoneNumber;
    private Map<String, Object> details = Collections.emptyMap();
    private String companyId;

    @CommandHandler
    public ResourceAggregate(ResourceCreationCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.resourceCreatedEvent(cmd));
        log.info("The creation of the resource with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onResourceCreated(ResourceCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.email = event.getEmail();
        this.phoneNumber = event.getPhoneNumber();
        this.details = event.getDetails();
        this.companyId = event.getCompanyId();
        log.info("Creation event of the resource with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleResourceUpdateCmd(ResourceUpdateCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.resourceUpdatedEvent(cmd));
        log.info("The update of the request with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onResourceUpdated(ResourceUpdatedEvent event) {
        this.id = event.getId().toString();
        this.description = event.getDescription();
        this.email = event.getEmail();
        this.phoneNumber = event.getPhoneNumber();
        this.details = event.getDetails();
        this.companyId = event.getCompanyId().toString();
        log.info("Update event of the resource with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleResourceDeletionCmd(ResourceDeletionCmd cmd) {
        AggregateLifecycle.apply(ResourceDeletedEvent.builder().id(cmd.getResourceId()).build());
        log.info("The deletion of the request with id [{}] have been successfully executed", cmd.getResourceId());
    }

    @EventSourcingHandler
    public void onResourceDeleted(ResourceDeletedEvent event) {
        this.id = event.getId().toString();
        AggregateLifecycle.markDeleted();
        log.info("Deletion event of the resource with id [{}] have been successfully send to the event bus", event.getId());
    }
}
