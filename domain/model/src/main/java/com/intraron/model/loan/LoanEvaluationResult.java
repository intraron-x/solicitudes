// Archivo: com/intraron/model/loan/LoanEvaluationResult.java
/**
 * @author intraron
 * Objeto de dominio para encapsular el resultado de la evaluación de un préstamo.
 * Pertenece a la capa de dominio y es independiente de la capa de API.
 */
package com.intraron.model.loan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanEvaluationResult {
    private final String evaluation;
    private final Double loanAmount;
    private final Integer loanTerm;
}