// Archivo: com/intraron/api/dto/LoanEvaluationResponseDTO.java
/**
 * @author intraron
 * DTO para la respuesta de evaluación de una solicitud de préstamo.
 * Contiene el resultado de la evaluación junto con los detalles de la solicitud.
 */
package com.intraron.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanEvaluationResponseDTO implements Serializable {
    private String evaluationResult;
    private Double loanAmount;
    private Integer loanTerm;
}
