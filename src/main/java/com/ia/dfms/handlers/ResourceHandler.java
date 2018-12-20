package com.ia.dfms.handlers;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.dfms.commands.creation.ArtifactCreationCmd;
import com.ia.dfms.commands.creation.CompanyCreationCmd;
import com.ia.dfms.commands.creation.RequestCreationCmd;
import com.ia.dfms.commands.creation.ResourceCreationCmd;
import com.ia.dfms.commands.creation.TaskCreationCmd;
import com.ia.dfms.commands.deletion.CompanyDeletionCmd;
import com.ia.dfms.commands.update.CompanyUpdateCmd;
import com.ia.dfms.util.AggregateUtil;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@RestController
public class ResourceHandler implements Handler {

    private final CommandGateway gateway;
    private final AggregateUtil util;

    public Mono<ServerResponse> resourceAdd(ServerRequest request) {
        final Mono<ResourceCreationCmd> dto = request.bodyToMono(ResourceCreationCmd.class);
        return dto.map(c -> ResourceCreationCmd.from(c)
                    .id(UUID.randomUUID().toString())
                    .build()
                    .validate(util))
                .flatMap(r -> response(r, gateway))
                .switchIfEmpty(ServerResponse.badRequest()
                        .body(Mono.just("Missing request body."), String.class));
    }

    public Mono<ServerResponse> requestAdd(ServerRequest request) {
        final Mono<RequestCreationCmd> dto = request.bodyToMono(RequestCreationCmd.class);
        return dto.map(c -> RequestCreationCmd.from(c)
                    .id(UUID.randomUUID().toString())
                    .build()
                    .validate(util))
                .flatMap(r -> response(r, gateway))
                .switchIfEmpty(ServerResponse.badRequest()
                        .body(Mono.just("Missing request body."), String.class));
    }

    public Mono<ServerResponse> artifactAdd(ServerRequest request) {
        final Mono<ArtifactCreationCmd> dto = request.bodyToMono(ArtifactCreationCmd.class);
        return dto.map(c -> ArtifactCreationCmd.from(c)
                    .id(UUID.randomUUID().toString())
                    .build()
                    .validate(util))
                .flatMap(r -> response(r, gateway))
                .switchIfEmpty(ServerResponse.badRequest()
                        .body(Mono.just("Missing request body."), String.class));
    }

    public Mono<ServerResponse> organisationAdd(ServerRequest request) {
        final Mono<CompanyCreationCmd> dto = request.bodyToMono(CompanyCreationCmd.class);
         return dto.map(c -> CompanyCreationCmd.from(c)
                     .id(UUID.randomUUID().toString())
                     .build()
                     .validate())
                .flatMap(r -> response(r, gateway))
                .switchIfEmpty(ServerResponse.badRequest()
                        .body(Mono.just("Missing request body."), String.class));
    }

    public Mono<ServerResponse> organisationUpdate(ServerRequest request) {
        final Mono<CompanyUpdateCmd> dto = request.bodyToMono(CompanyUpdateCmd.class);
        final String companyId = request.pathVariable("companyId");
        if (companyId == null) {
            return ServerResponse.badRequest()
                    .body(Mono.just("CompanyId is missing as a path variable."), String.class);
        }
        return dto.map(c -> CompanyUpdateCmd.from(c)
                    .id(companyId)
                    .build()
                    .validate(util))
                .flatMap(r -> response(r, gateway))
                .switchIfEmpty(ServerResponse.badRequest()
                        .body(Mono.just("Missing request body."), String.class));
    }

    public Mono<ServerResponse> organisationDelete(ServerRequest request) {
        final String companyId = request.pathVariable("companyId");
        if (companyId == null) {
            return ServerResponse.badRequest()
                    .body(Mono.just("CompanyId is missing as a path variable."), String.class);
        }
        return response(CompanyDeletionCmd.builder()
                .companyId(companyId)
                .build()
                .validate(util), gateway);
    }

    public Mono<ServerResponse> taskAdd(ServerRequest request) {
        final Mono<TaskCreationCmd> dto = request.bodyToMono(TaskCreationCmd.class);
        return dto.map(c -> TaskCreationCmd.from(c)
                    .id(UUID.randomUUID().toString())
                    .build()
                    .validate(util))
                .flatMap(r -> response(r, gateway))
                .switchIfEmpty(ServerResponse.badRequest()
                        .body(Mono.just("Missing request body."), String.class));
    }

}
