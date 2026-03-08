package br.com.petra.service.mapper;

import br.com.petra.domain.Investimento;
import br.com.petra.service.dto.InvestimentoResponseDTO;

public class InvestimentoResponseMapper extends AbstractDTOMapper {

    public InvestimentoResponseDTO toDTO(Investimento entity) {
        InvestimentoResponseDTO dto = new InvestimentoResponseDTO();


        // convert other attributes to dto like the example below

        dto.setId(entity.getId());
        dto.setMoeda(entity.getMoeda() == null ? null : entity.getMoeda().getCodigo());
        dto.setTipoInvestimento(entity.getTipoInvestimento() == null ? null : entity.getTipoInvestimento().getCodigo());
        dto.setTipoIndexacao(entity.getTipoIndexacao() == null ? null : entity.getTipoIndexacao().getCodigo());
        dto.setValorInvestido(formatBigDecimal(entity.getValorInvestido()));
        dto.setCpfInvestidor(leftPadCpf(entity.getCpfInvestidor()));
        dto.setDataCriacao(formatDateTime(entity.getCreatedAt()));
        dto.setDataAlteracao(formatDateTime(entity.getUpdatedAt()));

//        this.valorInvestido = formatBigDecimal(entity.getValorInvestido());
//        this.cpfInvestidor = leftPadCpf(entity.getCpfInvestidor());
//        this.moeda = entity.getMoeda() == null ? null : entity.getMoeda().getCodigo();
//        this.tipoInvestimento = entity.getTipoInvestimento() == null ? null : entity.getTipoInvestimento().getCodigo();
//        this.tipoIndexacao = entity.getTipoIndexacao() == null ? null : entity.getTipoIndexacao().getCodigo();
//        this.dataCriacao = formatDateTime(entity.getCreatedAt());
//        this.dataAlteracao = formatDateTime(entity.getUpdatedAt());

        return dto;
    }

}
