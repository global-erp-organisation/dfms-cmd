package com.ia.dfms.commands.update;

import java.time.LocalDateTime;
import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.ia.dfms.enums.RequestStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestTrackingUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    private String requestId;
    private String observation;
    private String resourceId;
    private LocalDateTime trackingTime;
    private RequestStatus requestStatus;
    
    public static RequestTrackingUpdateCmd from(RequestUpdateCmd cmd) {
        return  RequestTrackingUpdateCmd.builder()
                .id(UUID.randomUUID().toString())
                .observation("Request created successfully.")
                .requestId(cmd.getId())
                .requestStatus(RequestStatus.OPEN)
                .resourceId(cmd.getRequesterId())
                .build();
    }
}
