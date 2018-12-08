package com.ia.dfms.commands.creation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.dfms.validors.Validator;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreationCmd  implements Validator<List<String>, CompanyCreationCmd>{
    @TargetAggregateIdentifier
    private String id;
    private String name;
    @Builder.Default
    private Map<String, Object> details = Collections.emptyMap();
    
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
       return build(errors, this, errors.isEmpty());
    }
}
