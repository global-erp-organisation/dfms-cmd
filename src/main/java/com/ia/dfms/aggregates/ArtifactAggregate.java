package com.ia.dfms.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.ArtifactCreationCmd;
import com.ia.dfms.commands.deletion.ArtifactDeletionCmd;
import com.ia.dfms.commands.update.ArtifactUpdateCmd;
import com.ia.dfms.events.creation.ArtifactCreatedEvent;
import com.ia.dfms.events.deletion.ArtifactDeletedEvent;
import com.ia.dfms.events.update.ArtifactUpdatedEvent;
import com.ia.dfms.util.EventBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode
public class ArtifactAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private String uri;
    private String companyId;

    @CommandHandler
    public ArtifactAggregate(ArtifactCreationCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.artifactCreatedEvent(cmd));
        log.info("Artifact creation command [{}] succesfully apply.", cmd.getId());
    }

    @EventSourcingHandler
    public void onArtifactCreated(ArtifactCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.uri = event.getUri();
        this.companyId = event.getCompanyId();
        log.info("Creation event of the artifact with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleArtifactDeletionCmd(ArtifactDeletionCmd cmd) {
        AggregateLifecycle.apply(ArtifactDeletedEvent.builder().id(cmd.getArtifactId()).build());
        log.info("Artifact deletion command [{}] succesfully apply.", cmd.getArtifactId());
    }

    @EventSourcingHandler
    public void onArtifactDeleted(ArtifactDeletedEvent event) {
        this.id = event.getId();
        AggregateLifecycle.markDeleted();
        log.info("Deletion event of the artifact with id [{}] have been successfully send to the event bus", event.getId());
    }

    @CommandHandler
    public void handleArtifactUpdateCmd(ArtifactUpdateCmd cmd, EventBuilder builder) {
        AggregateLifecycle.apply(builder.artifactUpdatedEvent(cmd));
        log.info("Artifact update command [{}] succesfully apply.", cmd.getId());
    }

    @EventSourcingHandler
    public void onArtifactUpdate(ArtifactUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.uri = event.getUri();
        this.companyId = event.getCompanyId();
        log.info("Update event of the Artifact with id [{}] have been successfully send to the event bus", event.getId());
    }
}
