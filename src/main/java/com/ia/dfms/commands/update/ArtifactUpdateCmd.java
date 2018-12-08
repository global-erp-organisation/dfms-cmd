package com.ia.dfms.commands.update;

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
public class ArtifactUpdateCmd {
    @TargetAggregateIdentifier
    @NotEmpty(message = "ArtifactId shouldn't be null or empty")
    private String id;
    @NotEmpty(message = "Artifact description shouldn't be null or empty")
    private String description;
    @NotEmpty(message = "Artifact URI shouldn't be null or empty")
    private String uri;
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    private String companyId;
}
