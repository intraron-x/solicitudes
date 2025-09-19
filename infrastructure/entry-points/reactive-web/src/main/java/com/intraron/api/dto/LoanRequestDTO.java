// Archivo: com/intraron/api/dto/LoanRequestDTO.java
/**
 * @author intraron
 * DTO para la solicitud de un nuevo préstamo. Contiene los campos necesarios que el cliente envía
 * a través de la API.
 */
package com.intraron.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO implements Serializable {
    private String userName;
    private String userEmail; // Correo electrónico para identificar al usuario
    private Double loanAmount; // Monto del préstamo solicitado
    private Integer loanTerm;  // Plazo del préstamo en meses
}