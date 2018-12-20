package com.ia.dfms.commands.deletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.dfms.aggregates.CompanyAggregate;
import com.ia.dfms.util.AggregateUtil;
import com.ia.dfms.validors.CommandValidator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CompanyDeletionCmd extends CommandValidator<List<String>, CompanyDeletionCmd> {
    @TargetAggregateIdentifier
    private String companyId;

    @Override
    public Result<List<String>, CompanyDeletionCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(companyId)) {
            errors.add("CompanyId shouldn't be null or empty");
        }
        if (!util.aggregateGet(companyId, CompanyAggregate.class).isPresent()) {
            errors.add("No company found for id " + companyId);
        }
        return buildResult(errors, Optional.of(this), errors.isEmpty());
    }

}
