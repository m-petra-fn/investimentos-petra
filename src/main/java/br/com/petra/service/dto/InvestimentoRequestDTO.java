package br.com.petra.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InvestimentoRequestDTO(
        @NotBlank String valorInvestido,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpfInvestidor,
        @NotNull Long moedaId,
        @NotNull Long tipoInvestimentoId,
        @NotNull Long tipoIndexacaoId
) {
}

