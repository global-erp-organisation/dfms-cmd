package com.ia.dfms.commands.creation;

import java.util.Collection;
import java.util.Collections;

import javax.validation.constraints.NotEmpty;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TaskCreationCmd {
    @TargetAggregateIdentifier
    @NotEmpty(message = "TaskId shouldn't be null or empty")
    String id;
    @NotEmpty(message = "Task description shouldn't be null or empty")
    String description;
    @Builder.Default
    Collection<String> artifactIds = Collections.emptyList();
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    String companyId;
    
    
    public static TaskCreationCmdBuilder from(TaskCreationCmd cmd) {
        return TaskCreationCmd.builder()
                .artifactIds(cmd.getArtifactIds())
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .id(cmd.getId());
    }
}
