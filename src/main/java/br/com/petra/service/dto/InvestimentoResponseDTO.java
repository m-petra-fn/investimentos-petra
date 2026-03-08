package br.com.petra.service.dto;

import java.util.UUID;

public record InvestimentoResponseDTO(
        UUID id,
        String valorInvestido,
        String cpfInvestidor,
        Long moedaId,
        String moedaCodigo,
        Long tipoInvestimentoId,
        String tipoInvestimentoCodigo,
        Long tipoIndexacaoId,
        String tipoIndexacaoCodigo,
        String dataCriacao,
        String dataAlteracao
) {
}

