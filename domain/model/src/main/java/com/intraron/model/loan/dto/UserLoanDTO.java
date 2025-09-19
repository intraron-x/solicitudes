// Archivo: com/intraron/model/loan/dto/UserLoanDTO.java
/**
 * @author intraron
 * DTO para representar el usuario en el contexto del microservicio de solicitudes.
 * Contiene solo los datos relevantes para este dominio, desacoplando la
 * aplicación del modelo de datos del microservicio de autenticación.
 */
package com.intraron.model.loan.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoanDTO {
    private String correoElectronico;
    private Double salarioBase;
    private Set<String> roles;
}