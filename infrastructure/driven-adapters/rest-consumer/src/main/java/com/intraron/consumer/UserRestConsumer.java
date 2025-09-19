// Archivo: com/intraron/consumer/UserRestConsumer.java
/**
 * @author intraron
 * Implementación del puerto de salida UserGateway.
 * Esta clase es un Driven Adapter de tipo REST Consumer que se encarga de la comunicación
 * con el microservicio de autenticación.
 */
package com.intraron.consumer;

import com.intraron.model.loan.dto.UserLoanDTO;
import com.intraron.model.loan.gateways.UserGateway;
import com.intraron.consumer.dto.ExternalUserDTO; // Se crea un DTO para el mapeo
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

@Slf4j
@Service
//@RequiredArgsConstructor
public class UserRestConsumer implements UserGateway {
    private final WebClient client;

    // intraron: Se inyecta la URL del servicio desde las propiedades
    @Autowired
    public UserRestConsumer(@Value("${autenticacion.service.url:http://localhost:8080}") String autenticacionServiceUrl,
                            @Value("${intraron.internal.service-api-key}") String apiKey) {
                this.client = WebClient.builder()
                .baseUrl(autenticacionServiceUrl).defaultHeader("X-API-Key", apiKey)
                .build();
        log.info("URL servicio de terceros auth: {}", autenticacionServiceUrl);
    }

    /**
     * @author intraron
     * Implementa la búsqueda de un usuario por email llamando al endpoint
     * del microservicio de autenticaciones de forma reactiva.
     * @param email El correo electrónico del usuario.
     * @return Mono<UserLoanDTO> que emite el objeto de dominio del usuario.
     */
    @Override
    public Mono<UserLoanDTO> findUserByEmail(String email) {
        log.info("Llamando al servicio de autenticación para validar el usuario con email: {}", email);

        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users/by-email").queryParam("email", email).build())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> {
                    log.warn("Usuario con email {} no encontrado en el servicio de autenticación.", email);
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error al llamar al servicio de autenticación. Status: {}", clientResponse.statusCode());
                    return Mono.error(new IllegalStateException("Error en el servicio de autenticación."));
                })
                .bodyToMono(ExternalUserDTO.class)
                // intraron: Se mapea el objeto 'ExternalUserDTO' a 'UserLoanDTO' antes de devolverlo.
                .map(externalUser -> UserLoanDTO.builder()
                        .correoElectronico(externalUser.getCorreoElectronico())
                        .salarioBase(externalUser.getSalarioBase())
                        .roles(externalUser.getRoles())
                        .build());
    }
}