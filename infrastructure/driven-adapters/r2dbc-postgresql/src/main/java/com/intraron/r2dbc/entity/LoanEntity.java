// Archivo: com/intraron/r2dbc/entity/LoanEntity.java
/**
 * @author intraron
 * Entidad de persistencia para una solicitud de préstamo. Representa una fila en la tabla 'solicitudes'.
 */
package com.intraron.r2dbc.entity;

import com.intraron.model.loan.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitudes")
public class LoanEntity {
    @Id // intraron: El ID es generado automáticamente por la base de datos.
    private UUID id;

    @Column("user_email")
    private String userEmail;

    @Column("loan_amount")
    private Double loanAmount;

    @Column("loan_term")
    private Integer loanTerm;

    @Column("created_at")
    private OffsetDateTime createdat;

    @Column("fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @Column("loan_type")
    private String loanType;

    @Column("interest_rate")
    private Double interestRate;

    @Column("request_status")
    private String requestStatus;

    @Column("base_salary")
    private Double baseSalary;

    @Column("total_monthly_debt")
    private Double totalMonthlyDebt;

    public Loan toLoan() {
        return Loan.builder()
                .id(this.id)
                .userEmail(this.userEmail)
                .loanAmount(this.loanAmount)
                .loanTerm(this.loanTerm)
                .created_at(this.createdat)
                .fecha_creacion(this.fechaCreacion)
                .loanType(this.loanType)
                .interestRate(this.interestRate)
                .requestStatus(this.requestStatus)
                .baseSalary(this.baseSalary)
                .totalMonthlyDebt(totalMonthlyDebt)
                .build();
    }
}