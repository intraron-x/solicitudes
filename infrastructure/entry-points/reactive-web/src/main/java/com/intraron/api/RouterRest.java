/**
 * @author intraron
 * Esta clase es el enrutador de peticiones WebFlux. Define los endpoints y los asocia a los métodos
 * del manejador (Handler). Se ha ajustado para incluir el endpoint de registro de usuarios.
 */
package com.intraron.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperations;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Solicitudes", description = "Operaciones relacionadas con solicitudes de préstamo")
public class RouterRest {
    @Bean
    @RouterOperations({
            // Endpoint: POST /api/v1/solicitud
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    operation = @Operation(
                            summary = "Registrar una nueva solicitud de préstamo",
                            description = "Registra una solicitud de préstamo en el sistema",
                            operationId = "registerLoanRequest",
                            tags = {"Solicitudes"}
                    )
            ),
            // Endpoint: GET /api/v1/solicitud/{id}/evaluacion
            @RouterOperation(
                    path = "/api/v1/solicitud/{id}/evaluacion",
                    operation = @Operation(
                            summary = "Evaluar una solicitud de préstamo por ID",
                            description = "Realiza la evaluación de la solicitud basándose en el ID proporcionado",
                            operationId = "evaluateLoan",
                            tags = {"Solicitudes"}
                    )
            ),
            // Endpoint: GET /api/v1/getsolicitud
            @RouterOperation(
                    path = "/api/v1/getsolicitud",
                    operation = @Operation(
                            summary = "Obtener solicitudes para revisión manual",
                            description = "Obtiene una lista paginada de solicitudes que requieren revisión manual",
                            operationId = "getManualReviewLoansPaginated",
                            tags = {"Solicitudes"}
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), handler::registerLoanRequest)
                .andRoute(GET("/api/v1/solicitud/{id}/evaluacion"), handler::evaluateLoan)
                .andRoute(GET("/api/v1/getsolicitud").and(accept(MediaType.APPLICATION_JSON)), handler::getManualReviewLoansPaginated);
    }
}

