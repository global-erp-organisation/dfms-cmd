package com.ia.dfms.commands.creation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.ia.dfms.enums.RequestStatus;

import lombok.Builder;
import lombok.Value;
@Value
@Builder
public class RequestCreationCmd {
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
    
    public static RequestCreationCmdBuilder from(RequestCreationCmd cmd) {
        return RequestCreationCmd.builder()
                .id(cmd.getId())
                .requestDate(cmd.getRequestDate())
                .requestDetails(cmd.getRequestDetails())
                .requesterId(cmd.getRequesterId())
                .taskId(cmd.getTaskId())
                .artifactIds(cmd.getArtifactIds());
    }
}
