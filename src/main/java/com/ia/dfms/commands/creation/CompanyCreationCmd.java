package com.ia.dfms.commands.creation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;
import com.ia.dfms.validors.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Data
@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class CompanyCreationCmd  extends CommandValidator<List<String>, CompanyCreationCmd>{
    @TargetAggregateIdentifier
    String id;
    String name;
    @Builder.Default
    Map<String, Object> details = Collections.emptyMap();
    
    public static CompanyCreationCmdBuilder from(CompanyCreationCmd cmd) {
        return CompanyCreationCmd.builder()
                .id(cmd.getId())
                .name(cmd.getName())
                .details(cmd.getDetails());
    }

    @Override
    public Result<List<String>, CompanyCreationCmd> validate() {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("CompanyId shouldn't be null or empty");
        }
        if(StringUtil.isNullOrEmpty(name)) {
            errors.add("Company name shouldn't be null or empty");
        }
       return buildResult(errors, Optional.of(this), errors.isEmpty());
    }
}
