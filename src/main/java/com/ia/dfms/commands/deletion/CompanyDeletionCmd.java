package com.ia.dfms.commands.deletion;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDeletionCmd {
    @TargetAggregateIdentifier
    private String companyId;
}
