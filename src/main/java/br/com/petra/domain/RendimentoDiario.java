package br.com.petra.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "tb_rendimento_diario",
        uniqueConstraints = @UniqueConstraint(name = "uk_rendimento_investimento_data", columnNames = {"investimento_id", "data_referencia"})
)
public class RendimentoDiario extends AuditableEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "investimento_id", nullable = false)
    private Investimento investimento;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorRendido;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal irAtual;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal iofAtual;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorLiquido;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}

