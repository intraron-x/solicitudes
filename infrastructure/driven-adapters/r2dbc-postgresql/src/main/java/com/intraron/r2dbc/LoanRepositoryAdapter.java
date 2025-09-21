// Archivo: com/intraron/r2dbc/LoanRepositoryAdapter.java
/**
 * @author intraron
 * Adaptador que implementa el puerto del dominio (LoanRepository).
 * Maneja la lógica de persistencia y el mapeo de objetos de dominio a entidades de base de datos.
 */
package com.intraron.r2dbc;

import com.intraron.model.common.DomainPageable;
import com.intraron.model.loan.Loan;
import com.intraron.model.loan.gateways.LoanRepository;
import com.intraron.r2dbc.entity.LoanEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LoanRepositoryAdapter implements LoanRepository {

    private final LoanReactiveRepository loanReactiveRepository;
    private final DatabaseClient databaseClient;

    /**
     * @author intraron
     * Implementa la búsqueda de una solicitud por su ID.
     * @param id El ID de la solicitud a buscar.
     * @return Mono<Loan> que emite la solicitud de dominio encontrada.
     */
    @Override
    public Mono<Loan> findById(UUID id) {
        log.info("Buscando solicitud con ID: {}", id);
        return loanReactiveRepository.findById(id)
                .map(entity -> {
                    log.info("Solicitud encontrada con ID: {}", entity.getId());
                    // intraron: Se mapea la entidad de persistencia a un objeto de dominio
                    return Loan.builder()
                            .id(entity.getId())
                            .userEmail(entity.getUserEmail())
                            .loanAmount(entity.getLoanAmount())
                            .loanTerm(entity.getLoanTerm())
                            .loanType(entity.getLoanType())
                            .interestRate(entity.getInterestRate())
                            .requestStatus(entity.getRequestStatus())
                            .baseSalary(entity.getBaseSalary())
                            .totalMonthlyDebt(entity.getTotalMonthlyDebt())
                            .build();
                })
                .doOnError(e -> log.error("Error al buscar la solicitud con ID {}: {}", id, e.getMessage(), e));
    }

    @Override
    public Mono<Loan> save(Loan loan) {
        log.info("Iniciando el proceso de guardado para la solicitud del usuario: {}", loan.getUserEmail());

        LoanEntity loanEntity = LoanEntity.builder()
                .userEmail(loan.getUserEmail())
                .loanAmount(loan.getLoanAmount())
                .loanTerm(loan.getLoanTerm())
                .loanType(loan.getLoanType())
                .interestRate(loan.getInterestRate())
                .requestStatus(loan.getRequestStatus())
                .baseSalary(loan.getBaseSalary())
                .totalMonthlyDebt(loan.getTotalMonthlyDebt())
                .build();

        return loanReactiveRepository.save(loanEntity)
                .map(entity -> {
                    log.info("Solicitud guardada con éxito, ID: {}", entity.getId());
                    return Loan.builder()
                            .id(entity.getId())
                            .userEmail(entity.getUserEmail())
                            .loanAmount(entity.getLoanAmount())
                            .loanTerm(entity.getLoanTerm())
                            .loanType(entity.getLoanType())
                            .interestRate(entity.getInterestRate())
                            .requestStatus(entity.getRequestStatus())
                            .baseSalary(entity.getBaseSalary())
                            .totalMonthlyDebt(entity.getTotalMonthlyDebt())

                            .build();
                })
                .doOnError(e -> log.error("Error al guardar la solicitud: {}", e.getMessage(), e));
    }

    /**
     * @author intraron
     * Implementa el método para obtener un flujo de solicitudes filtradas por estados.
     * @param statuses Lista de estados por los que se desea filtrar.
     * @return Flux<Loan> que emite un flujo de solicitudes de dominio.
     */
    @Override
    public Flux<Loan> findAllByStatus(List<String> statuses) {
        log.info("Buscando solicitudes con los estados: {} y paginación: {}", statuses);

        return loanReactiveRepository.findByRequestStatusIn(statuses)
                .map(entity -> {
                    // intraron: Mapea la entidad de la base de datos al objeto de dominio
                    return Loan.builder()
                            .id(entity.getId())
                            .userEmail(entity.getUserEmail())
                            .loanAmount(entity.getLoanAmount())
                            .loanTerm(entity.getLoanTerm())
                            .loanType(entity.getLoanType())
                            .interestRate(entity.getInterestRate())
                            .requestStatus(entity.getRequestStatus())
                            .baseSalary(entity.getBaseSalary())
                            .totalMonthlyDebt(entity.getTotalMonthlyDebt())
                            .build();
                })
                .doOnError(e -> log.error("Error al buscar solicitudes por estado y paginación: {}", e.getMessage(), e));
    }

    /**
     * @author intraron
     * Implementa la búsqueda paginada de solicitudes por estado y devuelve el total de registros.
     * @param statuses La lista de estados a buscar.
     * @param pageable Los parámetros de paginación y ordenación.
     * @return Mono<Tuple2<List<Loan>, Long>> que contiene la lista de solicitudes y el total de registros.
     */
    @Override
    public Mono<Tuple2<List<Loan>, Long>> findAllByStatusPaginated(List<String> statuses, DomainPageable pageable) {
        log.info("1 Buscando solicitudes con los estados: {} con paginación", statuses);

        // Consulta para obtener los datos paginados
        String dataSql = "SELECT * FROM solicitudes WHERE request_status IN (:statuses) ORDER BY " + pageable.getSortBy() + " ASC LIMIT :limit OFFSET :offset";

        log.info("2 Query: {} con paginación", dataSql);

        int offset = pageable.getPage() * pageable.getSize();

        log.info("3 Buscando solicitudes con los estados: {}, ordenado por: {}, limit: {}, offset: {}", statuses, pageable.getSortBy(), pageable.getSize(), offset);

        Mono<List<Loan>> dataMono = databaseClient.sql(dataSql)
                .bind("statuses", statuses)
                .bind("limit", pageable.getSize())
                .bind("offset", offset)
                .map(row -> LoanEntity.builder()
                        .id(row.get("id", UUID.class))
                        .userEmail(row.get("user_email", String.class))
                        .loanAmount(row.get("loan_amount", Double.class))
                        .loanTerm(row.get("loan_term", Integer.class))
                        .createdat(row.get("created_at", OffsetDateTime.class))
                        .fechaCreacion(row.get("fecha_creacion", OffsetDateTime.class))
                        .loanType(row.get("loan_type", String.class))
                        .interestRate(row.get("interest_rate", Double.class))
                        .requestStatus(row.get("request_status", String.class))
                        .baseSalary(row.get("base_salary", Double.class))
                        .totalMonthlyDebt(row.get("total_monthly_debt", Double.class))
                        .build()
                )
                .all()
                .map(LoanEntity::toLoan)
                .collectList()
                .doOnSuccess(loans -> log.info("4 Resultados de la consulta: {}", loans));

        // Consulta para obtener el conteo total de registros
        String countSql = "SELECT COUNT(*) FROM solicitudes WHERE request_status IN (:statuses)";

        log.info("5 Query de conteo: {}, y los status: {}", countSql, statuses);

        Mono<Long> countMono = databaseClient.sql(countSql)
                .bind("statuses", statuses)
                .map(row -> row.get(0, Long.class))
                .one()
                .doOnSuccess(count -> log.info("6 Resultado de la consulta de conteo: {}", count));;

        // Se combinan los dos Monos en una tupla
        return Mono.zip(dataMono, countMono);
    }

    // Aquí está la implementación que falta y que el compilador exige
    @Override
    public Mono<Long> countAllByStatus(List<String> statuses) {
        log.info("Contando solicitudes con los estados: {}", statuses);
        return databaseClient.sql("SELECT COUNT(*) FROM solicitudes WHERE request_status IN (:statuses)")
                .bind("statuses", statuses)
                .map(row -> row.get(0, Long.class))
                .one();
    }
}