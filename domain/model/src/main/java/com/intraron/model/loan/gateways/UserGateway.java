// Archivo: com/intraron/model/loan/gateways/UserGateway.java
/**
 * @author intraron
 * Define el puerto de salida para la comunicación con el microservicio de autenticación (auth-service).
 * Este es un contrato para obtener información de usuarios, desacoplando el core de negocio
 * de la implementación técnica (e.g., una llamada HTTP).
 */
package com.intraron.model.loan.gateways;

import com.intraron.model.loan.dto.UserLoanDTO; // Se importa el nuevo DTO
import reactor.core.publisher.Mono;

public interface UserGateway {
    /**
     * Busca un usuario por su correo electrónico.
     * @param email El correo electrónico del usuario.
     * @return Mono<UserLoanDTO> que emite el objeto de dominio del usuario.
     */
    Mono<UserLoanDTO> findUserByEmail(String email);
}