package br.com.petra.service.dto;

import jakarta.validation.constraints.NotBlank;

public record RendimentoRequestDTO(
        @NotBlank String dataReferencia,
        @NotBlank String valorRendido,
        @NotBlank String irAtual,
        @NotBlank String iofAtual,
        @NotBlank String valorLiquido
) {
}

