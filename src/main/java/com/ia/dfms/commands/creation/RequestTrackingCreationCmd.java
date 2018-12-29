package com.ia.dfms.commands.creation;

import java.time.LocalDateTime;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.bson.types.ObjectId;

import com.ia.dfms.enums.RequestStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestTrackingCreationCmd {
    @TargetAggregateIdentifier
    private String id;
    private String requestId;
    private String observation;
    private String resourceId;
    private LocalDateTime trackingTime;
    private RequestStatus requestStatus;
    
    public static RequestTrackingCreationCmd from(RequestCreationCmd cmd) {
        return  RequestTrackingCreationCmd.builder()
                .id(new ObjectId().toHexString())
                .observation("Request created successfully.")
                .requestId(cmd.getId())
                .requestStatus(RequestStatus.OPEN)
                .resourceId(cmd.getRequesterId())
                .trackingTime(cmd.getRequestDate())
                .build();
    }
}
