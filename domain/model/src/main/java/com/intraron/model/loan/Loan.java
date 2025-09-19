// Archivo: com/intraron/model/loan/Loan.java
/**
 * @author intraron
 * Clase de dominio para una solicitud de préstamo. Representa la lógica de negocio.
 * Se utiliza el patrón de diseño Builder para su creación.
 */
package com.intraron.model.loan;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Loan {
    private UUID id;
    private String userEmail;
    private Double loanAmount;
    private Integer loanTerm;
    private String loanType;
    private Double interestRate;
    private String requestStatus;
    private Double baseSalary;
    private Double totalMonthlyDebt;
}