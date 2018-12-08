package com.ia.dfms.commands.update;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import com.ia.dfms.enums.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateCmd {
    @TargetAggregateIdentifier
    @NotEmpty(message = "RequestId shouldn't be null or empty")
    private String id;
    @NotEmpty(message = "taskId shouldn't be null or empty")
    private String taskId;
    @NotEmpty(message = "RequesterId shouldn't be null or empty")
    private String requesterId;
    @NotEmpty(message = "RequestDate shouldn't be null or empty")
    private LocalDateTime requestDate;
    @Builder.Default
    private Map<String, Object> requestDetails = Collections.emptyMap();
    private RequestStatus requestStatus;
    @Builder.Default
    private Collection<String> artifactIds = Collections.emptyList();
}
