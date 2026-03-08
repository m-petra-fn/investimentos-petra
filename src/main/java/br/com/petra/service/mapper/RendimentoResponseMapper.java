package br.com.petra.service.mapper;

import br.com.petra.domain.RendimentoDiario;
import br.com.petra.service.dto.RendimentoResponseDTO;
import org.springframework.data.util.Pair;

import java.util.List;

public class RendimentoResponseMapper extends AbstractDTOMapper {

    public RendimentoResponseDTO toDTO(RendimentoDiario entity) {
        RendimentoResponseDTO dto = new RendimentoResponseDTO();

        dto.setId(entity.getId());
        dto.setInvestimentoId(entity.getInvestimento() == null ? null : entity.getInvestimento().getId());
        dto.setDataReferencia(formatDate(entity.getDataReferencia()));
//        dto.setValorRendido(formatBigDecimal(entity.getValorRendido()));
//        dto.setIrAtual(formatBigDecimal(entity.getIrAtual()));
//        dto.setIofAtual(formatBigDecimal(entity.getIofAtual()));
//        dto.setValorLiquido(formatBigDecimal(entity.getValorLiquido()));
        dto.setDataCriacao(formatDateTime(entity.getCreatedAt()));
        dto.setDataAlteracao(formatDateTime(entity.getUpdatedAt()));

        formatDecimals(List.of(
                Pair.of(dto::setValorRendido, entity::getValorRendido),
                Pair.of(dto::setIrAtual, entity::getIrAtual),
                Pair.of(dto::setIofAtual, entity::getIofAtual),
                Pair.of(dto::setValorLiquido, entity::getValorLiquido)
        ));

        return dto;
    }

}
