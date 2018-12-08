package com.ia.dfms.handlers;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.dfms.commands.creation.RequestCreationCmd;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class RequestHandler {
    private final CommandGateway gateway;
    public Mono<ServerResponse> requestAdd(ServerRequest request) {
        final Mono<RequestCreationCmd> dto =
                request.bodyToMono(RequestCreationCmd.class)
                .map(r -> RequestCreationCmd.from(r)
                        .id(UUID.randomUUID().toString())
                        .build());
        final Mono<String> id = dto.map(c -> gateway.sendAndWait(c));
        return ServerResponse.accepted().body(id, String.class);
    }
}
