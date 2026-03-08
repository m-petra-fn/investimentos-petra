package br.com.petra.service.dto;

import java.util.UUID;

public record InvestimentoResponseDTO(
        UUID id,
        String valorInvestido,
        String cpfInvestidor,
        String moeda,
        String tipoInvestimento,
        String tipoIndexacao,
        String dataCriacao,
        String dataAlteracao
) {
}

