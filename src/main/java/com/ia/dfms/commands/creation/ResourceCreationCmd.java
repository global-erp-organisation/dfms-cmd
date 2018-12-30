package com.ia.dfms.commands.creation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
public class ResourceCreationCmd extends CommandValidator<List<String>, ResourceCreationCmd>{
    @TargetAggregateIdentifier
    @NotEmpty(message = "ResourceId shouldn't be null or empty")
    String id;
    @NotEmpty(message = "Resource description shouldn't be null or empty")
    String description;
    @NotEmpty(message = "Resource email shouldn't be null or empty")
    String email;
    String phoneNumber;
    @Builder.Default
    Map<String, Object> details = Collections.emptyMap();
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    String companyId;
    
    public static ResourceCreationCmdBuilder from(ResourceCreationCmd cmd) {
        return ResourceCreationCmd.builder()
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .email(cmd.getEmail())
                .id(cmd.getId())
                .phoneNumber(cmd.getPhoneNumber());
    }
    
    @Override
    public Result<List<String>, ResourceCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Resource identifier shouldn't be null or empty");
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Resource description shouldn't be null or empty");
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
