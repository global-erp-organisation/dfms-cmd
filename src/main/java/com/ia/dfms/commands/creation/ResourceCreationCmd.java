package com.ia.dfms.commands.creation;

import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;


@Builder
@Value
public class ResourceCreationCmd {
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
}
