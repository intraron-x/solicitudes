// Archivo: com/intraron/api/dto/LoanResponseDTO.java
/**
 * @author intraron
 * DTO para la respuesta de una lista de solicitudes de préstamo.
 * Contiene los campos específicos que el cliente necesita para el listado.
 */
package com.intraron.api.dto;

import com.intraron.model.loan.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO implements Serializable {
    private Double loanAmount;
    private Integer loanTerm;
    private String userEmail;
    private String userName;
    private OffsetDateTime createdat;
    private OffsetDateTime fechaCreacion;
    private String loanType;
    private Double interestRate;
    private String requestStatus;
    private Double baseSalary;
    private Double totalMonthlyDebt;

    /**
     * @author intraron
     * Mapea un objeto de dominio 'Loan' a un DTO de respuesta 'LoanResponseDTO'.
     * @param loan El objeto de dominio 'Loan' a mapear.
     * @return LoanResponseDTO con los datos de la solicitud.
     */
    public static LoanResponseDTO fromDomain(Loan loan) {
        return LoanResponseDTO.builder()
                .loanAmount(loan.getLoanAmount())
                .loanTerm(loan.getLoanTerm())
                .fechaCreacion(loan.getFecha_creacion())
                .createdat(loan.getCreated_at())
                .userEmail(loan.getUserEmail())
                .loanType(loan.getLoanType())
                .interestRate(loan.getInterestRate())
                .requestStatus(loan.getRequestStatus())
                .baseSalary(loan.getBaseSalary())
                .totalMonthlyDebt(loan.getTotalMonthlyDebt())
                .build();
    }
}