/**
 * @author intraron
 * Esta clase es el enrutador de peticiones WebFlux. Define los endpoints y los asocia a los métodos
 * del manejador (Handler). Se ha ajustado para incluir el endpoint de registro de usuarios.
 */
package com.intraron.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), handler::registerLoanRequest)
                .andRoute(GET("/api/v1/solicitud/{id}/evaluacion"), handler::evaluateLoan)
                .andRoute(GET("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), handler::getManualReviewLoansPaginated);
    }
}

