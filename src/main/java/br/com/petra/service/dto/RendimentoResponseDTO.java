package br.com.petra.service.dto;

import br.com.petra.domain.RendimentoDiario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RendimentoResponseDTO {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final UUID id;
    private final UUID investimentoId;
    private final String dataReferencia;
    private final String valorRendido;
    private final String irAtual;
    private final String iofAtual;
    private final String valorLiquido;
    private final String dataCriacao;
    private final String dataAlteracao;

    public RendimentoResponseDTO(RendimentoDiario entity) {
        this.id = entity.getId();
        this.investimentoId = entity.getInvestimento() == null ? null : entity.getInvestimento().getId();
        this.dataReferencia = formatDate(entity.getDataReferencia());
        this.valorRendido = formatBigDecimal(entity.getValorRendido());
        this.irAtual = formatBigDecimal(entity.getIrAtual());
        this.iofAtual = formatBigDecimal(entity.getIofAtual());
        this.valorLiquido = formatBigDecimal(entity.getValorLiquido());
        this.dataCriacao = formatDateTime(entity.getCreatedAt());
        this.dataAlteracao = formatDateTime(entity.getUpdatedAt());
    }

    public UUID getId() {
        return id;
    }

    public UUID getInvestimentoId() {
        return investimentoId;
    }

    public String getDataReferencia() {
        return dataReferencia;
    }

    public String getValorRendido() {
        return valorRendido;
    }

    public String getIrAtual() {
        return irAtual;
    }

    public String getIofAtual() {
        return iofAtual;
    }

    public String getValorLiquido() {
        return valorLiquido;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public String getDataAlteracao() {
        return dataAlteracao;
    }

    private static String formatBigDecimal(BigDecimal value) {
        return value == null ? null : value.setScale(4, RoundingMode.HALF_EVEN).toPlainString();
    }

    private static String formatDate(LocalDate value) {
        return value == null ? null : DATE_FORMATTER.format(value);
    }

    private static String formatDateTime(LocalDateTime value) {
        return value == null ? null : DATE_TIME_FORMATTER.format(value);
    }
}
