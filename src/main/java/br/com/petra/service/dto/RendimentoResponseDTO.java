package br.com.petra.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RendimentoResponseDTO {

    private UUID id;
    private UUID investimentoId;
    private String dataReferencia;
    private String valorRendido;
    private String irAtual;
    private String iofAtual;
    private String valorLiquido;
    private String dataCriacao;
    private String dataAlteracao;

}
