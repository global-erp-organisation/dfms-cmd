package com.ia.dfms.commands.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.dfms.aggregates.CompanyAggregate;
import com.ia.dfms.util.AggregateUtil;
import com.ia.dfms.validors.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class TaskCreationCmd extends CommandValidator<List<String>, TaskCreationCmd> {
    @TargetAggregateIdentifier
    String id;
    String description;
    @Builder.Default
    Collection<String> artifactIds = Collections.emptyList();
    String companyId;

    public static TaskCreationCmdBuilder from(TaskCreationCmd cmd) {
        return TaskCreationCmd.builder()
                .artifactIds(cmd.getArtifactIds())
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .id(cmd.getId());
    }

    @Override
    protected Result<List<String>, TaskCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Task identifier shouldn't be null or empty");
        }
        if (StringUtils.isEmpty(description)) {
            errors.add("Task description shouldn't be null or empty");
        }
        if (StringUtils.isEmpty(companyId)) {
            errors.add("Company identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(companyId, CompanyAggregate.class).isPresent()) {
                errors.add("The company with id " + companyId + " doesn't exist");
            }
        }
        return super.validate(util);
    }
}
