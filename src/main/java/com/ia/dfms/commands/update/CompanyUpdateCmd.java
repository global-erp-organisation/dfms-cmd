package com.ia.dfms.commands.update;

import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CompanyUpdateCmd {
    @TargetAggregateIdentifier
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    private String id;
    @NotEmpty(message = "Company name shouldn't be null or empty")
    private String name;
    @Builder.Default
    private Map<String, Object> details = Collections.emptyMap();
    
    public static CompanyUpdateCmdBuilder from(CompanyUpdateCmd cmd) {
        return CompanyUpdateCmd.builder()
                .details(cmd.getDetails())
                .id(cmd.getId())
                .name(cmd.getName());
    }
}
