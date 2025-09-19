// Archivo: com/intraron/r2dbc/LoanReactiveRepository.java
/**
 * @author intraron
 * Interfaz del repositorio de Spring Data para la entidad LoanEntity.
 * Spring se encarga de la implementación de los métodos CRUD de forma reactiva.
 */
package com.intraron.r2dbc;

import com.intraron.r2dbc.entity.LoanEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface LoanReactiveRepository extends ReactiveCrudRepository<LoanEntity, UUID> {
    // intraron: Se pueden agregar métodos de búsqueda personalizados si son necesarios.
    /**
     * @author intraron
     * Busca entidades de solicitud de préstamo cuyos estados se encuentren en una lista dada.
     * @param requestStatuses Lista de estados de solicitud a buscar.
     * @return Flux<LoanEntity> Un flujo que emite las entidades encontradas.
     */
    Flux<LoanEntity> findByRequestStatusIn(List<String> requestStatuses);
}