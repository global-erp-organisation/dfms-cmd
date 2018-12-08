package com.ia.dfms.aggregates;

import java.util.Collection;
import java.util.Collections;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import com.ia.dfms.commands.creation.TaskCreationCmd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Aggregate
public class TaskAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private Collection<RequestAggregate> requestAggregates = Collections.emptyList();
    private Collection<ArtifactAggregate> artifactAggregates = Collections.emptyList();

    @Autowired
    private EventSourcingRepository<CompanyAggregate> companyRepository;
    private EventSourcingRepository<ArtifactAggregate> artifactRepository;

    public TaskAggregate(TaskCreationCmd cmd) {
    }

}
