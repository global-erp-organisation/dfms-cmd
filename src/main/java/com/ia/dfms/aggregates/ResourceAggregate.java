package com.ia.dfms.aggregates;

import java.util.Collections;
import java.util.Map;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Aggregate
@NoArgsConstructor
public class ResourceAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private String email;
    private String phoneNumber;
    private Map<String, Object> details = Collections.emptyMap();
    private CompanyAggregate companyAggregate;
}
