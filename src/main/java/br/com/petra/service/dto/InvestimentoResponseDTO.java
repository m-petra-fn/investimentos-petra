package br.com.petra.service.dto;

import br.com.petra.domain.Investimento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class InvestimentoResponseDTO {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final UUID id;
    private final String valorInvestido;
    private final String cpfInvestidor;
    private final String moeda;
    private final String tipoInvestimento;
    private final String tipoIndexacao;
    private final String dataCriacao;
    private final String dataAlteracao;

    public InvestimentoResponseDTO(Investimento entity) {
        this.id = entity.getId();
        this.valorInvestido = formatBigDecimal(entity.getValorInvestido());
        this.cpfInvestidor = leftPadCpf(entity.getCpfInvestidor());
        this.moeda = entity.getMoeda() == null ? null : entity.getMoeda().getCodigo();
        this.tipoInvestimento = entity.getTipoInvestimento() == null ? null : entity.getTipoInvestimento().getCodigo();
        this.tipoIndexacao = entity.getTipoIndexacao() == null ? null : entity.getTipoIndexacao().getCodigo();
        this.dataCriacao = formatDateTime(entity.getCreatedAt());
        this.dataAlteracao = formatDateTime(entity.getUpdatedAt());
    }

    public UUID getId() {
        return id;
    }

    public String getValorInvestido() {
        return valorInvestido;
    }

    public String getCpfInvestidor() {
        return cpfInvestidor;
    }

    public String getMoeda() {
        return moeda;
    }

    public String getTipoInvestimento() {
        return tipoInvestimento;
    }

    public String getTipoIndexacao() {
        return tipoIndexacao;
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

    private static String formatDateTime(LocalDateTime value) {
        return value == null ? null : DATE_TIME_FORMATTER.format(value);
    }

    private static String leftPadCpf(String value) {
        if (value == null || value.length() >= 11) {
            return value;
        }
        return "0".repeat(11 - value.length()) + value;
    }
}
