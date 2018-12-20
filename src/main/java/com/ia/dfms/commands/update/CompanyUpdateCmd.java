package com.ia.dfms.commands.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.dfms.aggregates.CompanyAggregate;
import com.ia.dfms.util.AggregateUtil;
import com.ia.dfms.validors.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CompanyUpdateCmd extends CommandValidator<List<String>, CompanyUpdateCmd> {
    @TargetAggregateIdentifier
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    private String id;
    @NotEmpty(message = "Company name shouldn't be null or empty")
    private String name;
    @Builder.Default
    private Map<String, Object> details = Collections.emptyMap();

    public static CompanyUpdateCmdBuilder from(CompanyUpdateCmd cmd) {
        return CompanyUpdateCmd.builder().details(cmd.getDetails()).id(cmd.getId()).name(cmd.getName());
    }

    @Override
    public Result<List<String>, CompanyUpdateCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("CompanyId shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(id, CompanyAggregate.class).isPresent()) {
                errors.add("No Company found for id " + id);
            }
        }
        if (StringUtil.isNullOrEmpty(name)) {
            errors.add("Company name shouldn't be null or empty");
        }
        return buildResult(errors, Optional.of(this), errors.isEmpty());
    }
}
