package com.ia.dfms.aggregates;

import java.util.Optional;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.commands.creation.ArtifactCreationCmd;
import com.ia.dfms.commands.deletion.ArtifactDeletionCmd;
import com.ia.dfms.events.creation.ArtifactCreatedEvent;
import com.ia.dfms.events.creation.CompanyCreatedEvent;
import com.ia.dfms.events.deletion.ArtifactDeletedEvent;
import com.ia.dfms.util.AggregateUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Aggregate
@NoArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode
public class ArtifactAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private String uri;

    @CommandHandler
    public ArtifactAggregate(ArtifactCreationCmd cmd, AggregateUtil checker) {
        
        final Optional<CompanyAggregate> event = checker.aggregateGet(cmd.getCompanyId(), CompanyAggregate.class );
        if (event.isPresent()) {
            event.map(c -> {
                return AggregateLifecycle.apply(ArtifactCreatedEvent.builder()
                        .companyCreatedEvent(CompanyCreatedEvent.builder()
                                .details(c.getDetails())
                                .id(c.getId())
                                .name(c.getName())
                                .build())
                        .description(cmd.getDescription())
                        .id(cmd.getId())
                        .uri(cmd.getUri())
                        .build());
            });
            log.info("Artifact creation command [{}] succesfully apply.", cmd.getId());
        } else {
            log.info("Artifact creation command have been ignored. reason: company [{}] doesnt exist", cmd.getCompanyId());
        }
    }

    @EventSourcingHandler
    public void onArtifactCreation(ArtifactCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.uri = event.getUri();
    }

    @CommandHandler
    public void handleArtifactDeletionCmd(ArtifactDeletionCmd cmd) {
        AggregateLifecycle.apply(ArtifactDeletedEvent.builder().artifactId(cmd.getArtifactId()).build());
        log.info("Artifact deletion command [{}] succesfully apply.", cmd.getArtifactId());
    }
}
