package com.ia.dfms.aggregates;

import java.time.LocalDateTime;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.RequestTrackingCreationCmd;
import com.ia.dfms.commands.deletion.RequestTrackingDeletionCmd;
import com.ia.dfms.commands.update.RequestTrackingUpdateCmd;
import com.ia.dfms.enums.RequestStatus;
import com.ia.dfms.events.creation.RequestTrackingCreatedEvent;
import com.ia.dfms.events.deletion.RequestTrackingDeletedEvent;
import com.ia.dfms.events.update.RequestTrackingUpdatedEvent;
import com.ia.dfms.util.EventBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@Getter
@Slf4j
public class RequestTrackingAggregate {
    @AggregateIdentifier
    private String id;
    private String requestId;
    private String observation;
    private String resourceId;
    private LocalDateTime trackingTime;
    private RequestStatus requestStatus;

    @CommandHandler
    public RequestTrackingAggregate(RequestTrackingCreationCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.requestTrackingCreatedEvent(cmd));
        log.info("The creation of the request tracking with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onRequestTrackingCreated(RequestTrackingCreatedEvent event) {
        this.id = event.getId();
        this.observation = event.getObservation();
        this.requestId = event.getRequestId();
        this.requestStatus = event.getRequestStatus();
        this.trackingTime = event.getTrackingTime();
        this.resourceId = event.getManagerId();
        log.info("creation event of the Request tracking with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleRequestTrackingUpdateCmd(RequestTrackingUpdateCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.requestTrackingUpdatedEvent(cmd));
        log.info("The update of the request tracking with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onRequesttrackingUpdated(RequestTrackingUpdatedEvent event) {
        this.id = event.getId().toString();
        this.observation = event.getObservation();
        this.requestId = event.getRequestId().toString();
        this.requestStatus = event.getRequestStatus();
        this.trackingTime = event.getTrackingTime();
        this.resourceId = event.getManagerId().toString();
        log.info("Update event of the Request tracking with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleRequestTrackingDeletionCmd(RequestTrackingDeletionCmd cmd) {
        AggregateLifecycle.apply(RequestTrackingDeletedEvent.builder().id(cmd.getId()).build());
        log.info("The deletion of the request tracking with id [{}] have been successfully executed", cmd.getId());
    }

    public void onRequestTrackingDeleted(RequestTrackingDeletedEvent event) {
        this.id = event.getId().toString();
        AggregateLifecycle.markDeleted();
        log.info("Deleted event of the Request with id [{}] have been successfully send to the event bus", event.getId());
    }
}
