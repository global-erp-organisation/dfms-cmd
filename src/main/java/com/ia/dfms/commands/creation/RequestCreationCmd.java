package com.ia.dfms.commands.creation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ia.dfms.aggregates.ArtifactAggregate;
import com.ia.dfms.aggregates.ResourceAggregate;
import com.ia.dfms.aggregates.TaskAggregate;
import com.ia.dfms.enums.RequestStatus;
import com.ia.dfms.util.AggregateUtil;
import com.ia.dfms.validors.CommandValidator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Data
@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class RequestCreationCmd extends CommandValidator<List<String>, RequestCreationCmd> {
    @TargetAggregateIdentifier
    String id;
    String taskId;
    String requesterId;
    LocalDateTime requestDate;
    @Builder.Default
    Map<String, Object> requestDetails = Collections.emptyMap();
    RequestStatus requestStatus;
    @Builder.Default
    Collection<String> artifactIds = Collections.emptyList();

    public static RequestCreationCmdBuilder from(RequestCreationCmd cmd) {
        return RequestCreationCmd.builder().id(cmd.getId())
                .requestDate(cmd.getRequestDate() == null ? LocalDateTime.now() : cmd.getRequestDate())
                .requestDetails(cmd.getRequestDetails())
                .requesterId(cmd.getRequesterId()).taskId(cmd.getTaskId()).artifactIds(cmd.getArtifactIds());
    }

    @Override
    public Result<List<String>, RequestCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Request identifier shouldn't be null or empty");
        }
        if (StringUtils.isEmpty(taskId)) {
            errors.add("Task identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(taskId, TaskAggregate.class).isPresent()) {
                errors.add("The Task with id " + taskId + " doesnt exist");
            }
        }
        if (requestDate == null) {
            errors.add("Request date shouldn't be null or empty");
        }
        if (StringUtils.isEmpty(requesterId)) {
            errors.add("Requester identifier shouldnt be null or empty.");
        } else {
            if (!util.aggregateGet(requesterId, ResourceAggregate.class).isPresent()) {
                errors.add("The Resource with id " + requesterId + " doesnt exist");
            }
        }
        if (!CollectionUtils.isEmpty(artifactIds)) {
            artifactIds.stream().filter(a -> !util.aggregateGet(a, ArtifactAggregate.class).isPresent()).forEach(a -> {
                errors.add("The Artifact with id " + a + " doesnt exist");
            });
        }
        return buildResult(errors, Optional.of(this), errors.isEmpty());
    }
}
