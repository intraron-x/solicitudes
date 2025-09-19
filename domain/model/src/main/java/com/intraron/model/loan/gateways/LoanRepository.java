// Archivo: com/intraron/model/loan/gateways/LoanRepository.java
/**
 * @author intraron
 * Puerto de salida para el dominio de solicitudes de préstamo. Define la funcionalidad
 * que el dominio necesita de la capa de infraestructura, como guardar una solicitud.
 */
package com.intraron.model.loan.gateways;

import com.intraron.model.common.DomainPageable;
import com.intraron.model.loan.Loan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.UUID;

/**
 * @author intraron
 * Esta interfaz es un puerto de la capa de dominio para la persistencia de solicitudes de préstamo.
 * Define las operaciones que la capa de infraestructura debe implementar.
 */
public interface LoanRepository {
    /**
     * Guarda una nueva solicitud de préstamo en la base de datos.
     * @param loan El objeto de la solicitud de préstamo a guardar.
     * @return Mono<Loan> Un Mono que emite el objeto de la solicitud de préstamo guardada.
     */
    Mono<Loan> save(Loan loan);

    /**
     * Busca una solicitud de préstamo por su ID.
     * @param id El ID de la solicitud.
     * @return Mono<Loan> Un Mono que emite la solicitud de préstamo encontrada, o un Mono vacío si no existe.
     */
    Mono<Loan> findById(UUID id);

    /**
     * Obtiene una lista de solicitudes de préstamo por sus estados.
     * @param statuses La lista de estados por los que se debe buscar.
     * @return Flux<Loan> Un Flux que emite las solicitudes de préstamo encontradas.
     */
    Flux<Loan> findAllByStatus(List<String> statuses);

    /**
     * @author intraron
     * Busca solicitudes de préstamo por estado con paginación,
     * devolviendo una tupla que contiene la lista de préstamos y el conteo total.
     * @param statuses La lista de estados a filtrar.
     * @param pageable El objeto de paginación con la página, tamaño y orden.
     * @return Mono<Tuple2<List<Loan>, Long>> que emite la lista paginada y el total de registros.
     */
    Mono<Tuple2<List<Loan>, Long>> findAllByStatusPaginated(List<String> statuses, DomainPageable pageable);

    /**
     * Obtiene el conteo total de solicitudes de préstamo por sus estados.
     * @param statuses La lista de estados por los que se debe buscar.
     * @return Mono<Long> Un Mono que emite el conteo total de solicitudes.
     */
    Mono<Long> countAllByStatus(List<String> statuses);
}