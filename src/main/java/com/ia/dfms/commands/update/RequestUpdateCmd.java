package com.ia.dfms.commands.update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.dfms.aggregates.ArtifactAggregate;
import com.ia.dfms.aggregates.TaskAggregate;
import com.ia.dfms.commands.creation.RequestCreationCmd;
import com.ia.dfms.commands.creation.RequestCreationCmd.RequestCreationCmdBuilder;
import com.ia.dfms.enums.RequestStatus;
import com.ia.dfms.util.AggregateUtil;
import com.ia.dfms.validors.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class RequestUpdateCmd extends CommandValidator<List<String>, RequestUpdateCmd> {
    @TargetAggregateIdentifier
    @NotEmpty(message = "RequestId shouldn't be null or empty")
    String id;
    @NotEmpty(message = "taskId shouldn't be null or empty")
    String taskId;
    @NotEmpty(message = "RequesterId shouldn't be null or empty")
    String requesterId;
    @NotEmpty(message = "RequestDate shouldn't be null or empty")
    LocalDateTime requestDate;
    @Builder.Default
    Map<String, Object> requestDetails = Collections.emptyMap();
    RequestStatus requestStatus;
    @Builder.Default
    Collection<String> artifactIds = Collections.emptyList();
    
    
    public static RequestCreationCmdBuilder from(RequestUpdateCmd cmd) {
        return RequestCreationCmd.builder()
                .id(cmd.getId())
                .requestDate(cmd.getRequestDate())
                .requestDetails(cmd.getRequestDetails())
                .requesterId(cmd.getRequesterId())
                .taskId(cmd.getTaskId())
                .artifactIds(cmd.getArtifactIds());
    }

    @Override
    public Result<List<String>, RequestUpdateCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("RequestId shouldn't be null or empty");
        }
        if (StringUtils.isEmpty(taskId)) {
            errors.add("TaskId shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(taskId, TaskAggregate.class).isPresent()) {
                errors.add("The Task with id " + taskId + " doesnt exist");
            }
        }
        if (requestDate == null) {
            errors.add("Request date shouldn't be null or empty");
        }
        if (!artifactIds.isEmpty()) {
            artifactIds.stream().forEach(a -> {
                if (!util.aggregateGet(a, ArtifactAggregate.class).isPresent()) {
                    errors.add("The Artifact with id " + a + " doesnt exist");
                }
            });
        }
        return buildResult(errors, Optional.of(this), errors.isEmpty());
    }
}
