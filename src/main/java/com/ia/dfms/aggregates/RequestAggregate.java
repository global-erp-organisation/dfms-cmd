package com.ia.dfms.aggregates;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.dfms.enums.RequestStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Aggregate
@NoArgsConstructor
public class RequestAggregate {
    @AggregateIdentifier
    private String id;
    private TaskAggregate taskAggregate;
    private ResourceAggregate requester;
    private LocalDateTime requestDate;
    private Map<String, Object> requestDetails = Collections.emptyMap();
    private RequestStatus requestStatus;
    private Collection<ArtifactAggregate> artifactAggregates = Collections.emptyList();
}
