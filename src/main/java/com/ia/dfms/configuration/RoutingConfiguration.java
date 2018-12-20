package com.ia.dfms.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.dfms.handlers.ResourceHandler;

@Configuration
@EnableWebFlux
public class RoutingConfiguration {

    @Bean
    RouterFunction<ServerResponse> resourceRoutes(ResourceHandler handler) {
        return RouterFunctions.route(POST("/companies"), handler::organisationAdd)
                .andRoute(DELETE("/companies/{companyId}"), handler::organisationDelete)
                .andRoute(PATCH("/companies/{companyId}"), handler::organisationUpdate)
                .andRoute(POST("/artifacts"), handler::artifactAdd)
                .andRoute(POST("/tasks"), handler::taskAdd)
                .andRoute(POST("/resources"), handler::resourceAdd)
                .andRoute(POST("/requests"), handler::requestAdd);
    }
}
