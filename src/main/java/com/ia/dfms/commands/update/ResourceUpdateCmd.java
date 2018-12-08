package com.ia.dfms.commands.update;

import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceUpdateCmd {
    @TargetAggregateIdentifier
    @NotEmpty(message = "ResourceId shouldn't be null or empty")
    private String id;
    @NotEmpty(message = "Resource description shouldn't be null or empty")
    private String description;
    @NotEmpty(message = "Resource email shouldn't be null or empty")
    private String email;
    private String phoneNumber;
    @Builder.Default
    private Map<String, Object> details = Collections.emptyMap();
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    private String companyId;
}
