package br.com.petra.service.dto;

import java.util.UUID;

public record RendimentoResponseDTO(
        UUID id,
        UUID investimentoId,
        String dataReferencia,
        String valorRendido,
        String irAtual,
        String iofAtual,
        String valorLiquido,
        String dataCriacao,
        String dataAlteracao
) {
}

