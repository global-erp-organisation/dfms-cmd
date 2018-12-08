package com.ia.dfms.handlers;

import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.dfms.validors.Validator;

import reactor.core.publisher.Mono;

public interface Handler {

    default <V> Mono<ServerResponse> response(Validator.Result<List<String>, V> result, CommandGateway gateway) {
        if (result.getIsValid()) {
            final String id = gateway.sendAndWait(result.getValidated());
            return ServerResponse.accepted().body(id == null ? Mono.empty() : Mono.just(id), String.class);
        } else {
            return ServerResponse.badRequest().body(Mono.just(result.getErrors()), List.class);
        }
    }
}
