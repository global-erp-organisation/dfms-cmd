package com.ia.dfms.commands.creation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.dfms.validors.Validator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ArtifactCreationCmd  implements Validator<List<String>, ArtifactCreationCmd>{
    @TargetAggregateIdentifier
    String id;
    String description;
    @NotEmpty(message = "Artifact URI shouldn't be null or empty")
    String uri;
    @NotEmpty(message = "CompanyId shouldn't be null or empty")
    String companyId;
    
    public static ArtifactCreationCmdBuilder from(ArtifactCreationCmd cmd) {
        return ArtifactCreationCmd.builder()
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .uri(cmd.getUri());
    }
    
    
    @Override
    public Result<List<String>, ArtifactCreationCmd> validate() {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("ArtifactId shouldn't be null or empty");
        }
        if(StringUtil.isNullOrEmpty(description)) {
            errors.add("Artifact description shouldn't be null or empty");
        }
        if(StringUtil.isNullOrEmpty(uri)) {
            errors.add("Artifact URI shouldn't be null or empty");
        }
        if(StringUtil.isNullOrEmpty(companyId)) {
            errors.add("CompanyId shouldn't be null or empty");
        }
       return build(errors, this, errors.isEmpty());
    }

}
