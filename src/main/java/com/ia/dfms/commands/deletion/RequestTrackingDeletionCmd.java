package com.ia.dfms.commands.deletion;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTrackingDeletionCmd {
    @TargetAggregateIdentifier
    private String id;
}
