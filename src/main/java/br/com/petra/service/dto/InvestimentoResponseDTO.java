package br.com.petra.service.dto;

import br.com.petra.domain.Investimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvestimentoResponseDTO {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private UUID id;
    private String valorInvestido;
    private String cpfInvestidor;
    private String moeda;
    private String tipoInvestimento;
    private String tipoIndexacao;
    private String dataCriacao;
    private String dataAlteracao;

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


}
