// Archivo: com/intraron/consumer/dto/ExternalUserDTO.java
/**
 * @author intraron
 * DTO para el mapeo de la respuesta del microservicio de autenticación.
 * Es una clase interna del adaptador de infraestructura para desacoplar
 * el modelo de dominio de la comunicación externa.
 */
package com.intraron.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // Importante para ignorar campos que no necesitamos
public class ExternalUserDTO {
    private String id;
    private String correoElectronico;
    private Double salarioBase;
    private Set<String> roles;
}