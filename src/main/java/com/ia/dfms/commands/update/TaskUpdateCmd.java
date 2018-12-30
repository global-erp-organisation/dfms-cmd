package com.ia.dfms.commands.update;

import java.util.Collection;
import java.util.Collections;

import javax.validation.constraints.NotEmpty;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateCmd {
    @TargetAggregateIdentifier
    @NotEmpty(message = "TaskId shouldn't be null or empty")
    private String id;
    @NotEmpty(message = "Task description shouldn't be null or empty")
    private String description;
    @Builder.Default
    private Collection<String> artifactIds = Collections.emptyList();
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    private String companyId;
}
