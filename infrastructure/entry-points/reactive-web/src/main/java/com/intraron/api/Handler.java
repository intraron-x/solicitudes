/**
 * @author intraron
 * Este es el manejador (Handler) de las peticiones WebFlux. Contiene la lógica para
 * procesar la solicitud de registro de usuarios y se encarga de llamar al caso de uso.
 */

package com.intraron.api;

import com.intraron.api.dto.LoanEvaluationResponseDTO;
import com.intraron.api.dto.LoanRequestDTO;
import com.intraron.api.dto.LoanResponseDTO;
import com.intraron.model.common.DomainPageable;
import com.intraron.model.common.PaginatedResponse;
import com.intraron.model.loan.Loan;
import com.intraron.usecase.loan.LoanUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final LoanUseCase loanUseCase;

    /**
     * @author intraron
     * Maneja la petición para obtener una lista de solicitudes de préstamo para revisión manual con paginación.
     * @param serverRequest La petición del servidor, que contiene los query parameters de paginación.
     * @return Mono<ServerResponse> con la lista paginada de solicitudes.
     */
    public Mono<ServerResponse> getManualReviewLoansPaginated(ServerRequest serverRequest) {
        log.debug("Petición para obtener solicitudes de revisión manual recibida.");

        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
        String sortBy = serverRequest.queryParam("sortby").orElse("id");

        DomainPageable pageable = new DomainPageable(page, size, sortBy);

        // Se llama al caso de uso que devuelve una tupla con la lista de préstamos y el conteo total.
        return loanUseCase.getManualReviewLoansPaginated(pageable)
                .flatMap(tuple -> {
                    // intraron: Se extrae la lista de préstamos de la tupla.
                    List<Loan> loans = tuple.getT1();
                    // intraron: Se extrae el conteo total de la tupla.
                    Long totalElements = tuple.getT2();

                    log.info("Se encontraron {} solicitudes para revisión manual, total de registros: {}.", loans.size(), totalElements);

                    // intraron: Se construye la respuesta DTO que incluye la información de paginación.
                    PaginatedResponse<LoanResponseDTO> response = new PaginatedResponse<>();
                    response.setContent(loans.stream().map(LoanResponseDTO::fromDomain).collect(Collectors.toList()));
                    response.setTotalElements(totalElements);
                    response.setTotalPages((long) Math.ceil((double) totalElements / pageable.getSize()));
                    response.setCurrentPage(pageable.getPage());
                    response.setPageSize(pageable.getSize());

                    return ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                })
                .doOnError(e -> log.error("Error al obtener solicitudes para revisión manual: {}", e.getMessage()));
    }

    /**
     * @author intraron
     * Maneja la petición para evaluar una solicitud de préstamo por su ID.
     * Mapea el resultado del dominio a un DTO de respuesta.
     * @param serverRequest La petición del servidor, que contiene el ID en la ruta.
     * @return Mono<ServerResponse> Una respuesta HTTP reactiva con el resultado de la evaluación.
     */
    public Mono<ServerResponse> evaluateLoan(ServerRequest serverRequest) {
        log.info("Petición de evaluación de solicitud de préstamo recibida.");
        String loanId = serverRequest.pathVariable("id");
        log.info("ID Prestamo {}",loanId);

        return Mono.just(loanId)
                .map(UUID::fromString)
                .flatMap(loanUseCase::evaluateLoan)
                .flatMap(evaluationResult -> { // intraron: Aquí se recibe el objeto de dominio
                    log.info("Evaluación completada. Mapeando resultado a DTO.");
                    // intraron: Se mapea el objeto de dominio a un DTO de respuesta
                    LoanEvaluationResponseDTO responseDTO = LoanEvaluationResponseDTO.builder()
                            .evaluationResult(evaluationResult.getEvaluation())
                            .loanAmount(evaluationResult.getLoanAmount())
                            .loanTerm(evaluationResult.getLoanTerm())
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseDTO);
                });
                /*.onErrorResume(IllegalArgumentException.class, e -> {
                    log.warn("Validación fallida en la solicitud: {}", e.getMessage());
                    return ServerResponse.badRequest().bodyValue(e.getMessage());
                })
                .onErrorResume(e -> {
                    log.error("Error inesperado al evaluar la solicitud: {}", e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error interno al procesar la solicitud.");
                });*/
    }

    /**
     * @author intraron
     * Maneja la petición para registrar una nueva solicitud de préstamo.
     * Mapea el DTO de entrada al modelo de dominio y llama al caso de uso.
     * @param serverRequest La petición del servidor.
     * @return Mono<ServerResponse> Una respuesta HTTP reactiva.
     */
    public Mono<ServerResponse> registerLoanRequest(ServerRequest serverRequest) {
        log.info("Petición de registro de solicitud de préstamo recibida.");

        // Se obtiene el email del usuario logueado del contexto de seguridad.
        Mono<String> loggedInUserEmailMono = ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (UserDetails) securityContext.getAuthentication().getPrincipal())
                .map(UserDetails::getUsername);

        return Mono.zip(serverRequest.bodyToMono(LoanRequestDTO.class), loggedInUserEmailMono)
                .flatMap(tuple -> {

                    LoanRequestDTO requestDTO = tuple.getT1();
                    String loggedInUserEmail = tuple.getT2();

                    log.info("Mapeando LoanRequestDTO a Loan.");
                    // intraron: AQUI se realiza el mapeo.
                    Loan loan = Loan.builder()
                            .userEmail(requestDTO.getUserEmail())
                            .loanAmount(requestDTO.getLoanAmount())
                            .loanTerm(requestDTO.getLoanTerm())
                            .requestStatus("PENDIENTE_REVISION")
                            .build();

                    log.info("Correo electronico optenido de SS {}.", loggedInUserEmail);

                    // intraron: Se pasa el objeto de dominio al caso de uso.
                    return loanUseCase.save(loan,loggedInUserEmail)
                            .flatMap(savedLoan -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(savedLoan));
                            /*.onErrorResume(IllegalArgumentException.class, e -> {
                                log.warn("Validación fallida en la solicitud: {}", e.getMessage());
                                return ServerResponse.badRequest().bodyValue(e.getMessage());
                            })
                            .onErrorResume(e -> {
                                log.error("Error al registrar solicitud: {}", e.getMessage());
                                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error interno al procesar la solicitud.");
                            });*/
                });
    }
}
