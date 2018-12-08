package com.ia.dfms.commands.deletion;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactDeletionCmd {
    @TargetAggregateIdentifier
    private String artifactId;
}
