package com.ia.dfms.aggregates;

import java.util.Collection;
import java.util.Collections;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.TaskCreationCmd;
import com.ia.dfms.commands.deletion.TaskDeletionCmd;
import com.ia.dfms.commands.update.TaskUpdateCmd;
import com.ia.dfms.events.creation.TaskCreatedEvent;
import com.ia.dfms.events.deletion.TaskDeletedEvent;
import com.ia.dfms.events.update.TaskUpdatedEvent;
import com.ia.dfms.util.EventBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@Slf4j
public class TaskAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private String companyId;
    private Collection<String> artifactIds = Collections.emptyList();

    @CommandHandler
    public TaskAggregate(TaskCreationCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.taskCreatedEvent(cmd));
        log.info("The task of the request with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onTaskCreation(TaskCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.companyId = event.getCompanyId();
        this.artifactIds = event.getArtifactIds();
        log.info("Creation event of the Task with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void onTaskUpdate(TaskUpdateCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.taskUpdatedEvent(cmd));
        log.info("The update of the request with id [{}] have been successfully executed", cmd.getId());
    }

    @EventSourcingHandler
    public void onTaskUpdated(TaskUpdatedEvent event) {
        this.id = event.getId().toString();
        this.description = event.getDescription();
        this.companyId = event.getCompanyId();
        this.artifactIds = event.getArtifactIds();
        log.info("Update event of the Company with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleTaskDeletionCmd(TaskDeletionCmd cmd) {
        AggregateLifecycle.apply(TaskDeletedEvent.builder().id(cmd.getTaskId()).build());
        log.info("The deletion of the request with id [{}] have been successfully executed", cmd.getTaskId());
    }

    @EventSourcingHandler
    public void onTaskDeleted(TaskDeletedEvent event) {
        this.id = event.getId().toString();
        AggregateLifecycle.markDeleted();
    }
}
