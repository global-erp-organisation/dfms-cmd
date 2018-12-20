package com.ia.dfms.handlers;

import java.util.List;
import java.util.Optional;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.dfms.validors.CommandValidator;

import reactor.core.publisher.Mono;

public interface Handler {

    default <V> Mono<ServerResponse> response(CommandValidator.Result<List<String>, V> result, CommandGateway gateway) {
        if (result.getIsValid()) {
            final Optional<V> r = result.getValidated();
            if (r.isPresent()) {
                final V command = r.get();
                final String id = gateway.sendAndWait(command);
                return ServerResponse.accepted()
                        .body(id == null ? Mono.just(command.getClass().getSimpleName() + " successfully applied.") : Mono.just(id), String.class);
            } else {
                return ServerResponse.badRequest().body(Mono.just("No error message found. It sound like a default validator operation have been used."),
                        String.class);
            }
        } else {
            return ServerResponse.badRequest().body(Mono.just(result.getErrors()), List.class);
        }
    }
}
