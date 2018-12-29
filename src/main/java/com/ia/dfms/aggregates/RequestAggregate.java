package com.ia.dfms.aggregates;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.RequestCreationCmd;
import com.ia.dfms.commands.creation.RequestTrackingCreationCmd;
import com.ia.dfms.commands.deletion.RequestDeletionCmd;
import com.ia.dfms.commands.update.RequestUpdateCmd;
import com.ia.dfms.enums.RequestStatus;
import com.ia.dfms.events.creation.RequestCreatedEvent;
import com.ia.dfms.events.deletion.RequestDeletedEvent;
import com.ia.dfms.events.update.RequestUpdatedEvent;
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
public class RequestAggregate {
    @AggregateIdentifier
    private String id;
    private String taskId;
    private String requesterId;
    private LocalDateTime requestDate;
    private Map<String, Object> requestDetails = Collections.emptyMap();
    private RequestStatus requestStatus;
    private Collection<String> artifactIds = Collections.emptyList();

    @CommandHandler
    public RequestAggregate(RequestCreationCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.requestCreateEvent(cmd)).andThen(() -> {
            try {
                AggregateLifecycle.createNew(RequestTrackingAggregate.class,
                        () -> new RequestTrackingAggregate(RequestTrackingCreationCmd.from(cmd), builder));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.info("The creation of the request with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onRequestCreated(RequestCreatedEvent event) {
        this.id = event.getId().toString();
        this.taskId = event.getTaskId().toString();
        this.requesterId = event.getRequesterId().toString();
        this.requestDate = event.getRequestDate();
        this.requestDetails = event.getRequestDetails();
        this.requestStatus = event.getRequestStatus();
        this.artifactIds = event.getArtifactIds();
        log.info("Creation event of the Request with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleRequestUpdateCmd(RequestUpdateCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.requestUpdateEvent(cmd));
        log.info("The update of the request with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onRequestUpdate(RequestUpdatedEvent event) {
        this.id = event.getId().toString();
        this.taskId = event.getTaskId().toString();
        this.requesterId = event.getRequesterId().toString();
        this.requestDate = event.getRequestDate();
        this.requestDetails = event.getRequestDetails();
        this.requestStatus = event.getRequestStatus();
        this.artifactIds = event.getArtifactIds();
        log.info("Update event of the Request with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleRequestDeletionCmd(RequestDeletionCmd cmd) {
        AggregateLifecycle.apply(RequestDeletedEvent.builder().id(cmd.getRequestId()).build());
        log.info("The deletion of the request with id [{}] have been successfully executed", cmd.getRequestId());
    }

    public void onRequestDeleted(RequestDeletedEvent event) {
        this.id = event.getId().toString();
        AggregateLifecycle.markDeleted();
        log.info("Deletion event of the Request with id [{}] have been successfully send to the event bus", event.getId());
    }
}
