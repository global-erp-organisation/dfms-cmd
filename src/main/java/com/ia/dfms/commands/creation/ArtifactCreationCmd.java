package com.ia.dfms.commands.creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.dfms.aggregates.CompanyAggregate;
import com.ia.dfms.util.AggregateUtil;
import com.ia.dfms.validors.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class ArtifactCreationCmd extends CommandValidator<List<String>, ArtifactCreationCmd> {
    @TargetAggregateIdentifier
    String id;
    String description;
    @NotEmpty(message = "Artifact URI shouldn't be null or empty")
    String uri;
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    String companyId;
    public static ArtifactCreationCmdBuilder from(ArtifactCreationCmd cmd) {
        return ArtifactCreationCmd.builder()
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .uri(cmd.getUri());
    }

    @Override
    public Result<List<String>, ArtifactCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("ArtifactId shouldn't be null or empty");
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Artifact description shouldn't be null or empty");
        }
        if (StringUtil.isNullOrEmpty(uri)) {
            errors.add("Artifact URI shouldn't be null or empty");
        }
        if (StringUtil.isNullOrEmpty(companyId)) {
            errors.add("Company identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(companyId, CompanyAggregate.class).isPresent()) {
                errors.add("The company with id " + companyId + " doesnt exist");
            }
        }
        return buildResult(errors, Optional.of(this), errors.isEmpty());
    }

}
