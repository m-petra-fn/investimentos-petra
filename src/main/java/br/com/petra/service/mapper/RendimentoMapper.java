package br.com.petra.service.mapper;

import br.com.petra.domain.RendimentoDiario;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RendimentoMapper extends AbstractMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "investimento", ignore = true)
    @Mapping(target = "dataReferencia", source = "dataReferencia", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "valorRendido", source = "valorRendido", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "irAtual", source = "irAtual", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "iofAtual", source = "iofAtual", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "valorLiquido", source = "valorLiquido", qualifiedByName = "stringToBigDecimal")
    RendimentoDiario toEntity(RendimentoRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "investimento", ignore = true)
    @Mapping(target = "dataReferencia", source = "dataReferencia", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "valorRendido", source = "valorRendido", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "irAtual", source = "irAtual", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "iofAtual", source = "iofAtual", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "valorLiquido", source = "valorLiquido", qualifiedByName = "stringToBigDecimal")
    void updateEntityFromDto(RendimentoRequestDTO dto, @MappingTarget RendimentoDiario entity);

    @Mapping(target = "investimentoId", source = "investimento.id")
    @Mapping(target = "dataReferencia", source = "dataReferencia", qualifiedByName = "localDateToString")
    @Mapping(target = "valorRendido", source = "valorRendido", qualifiedByName = "bigDecimalToStringFourDecimalPlaces")
    @Mapping(target = "irAtual", source = "irAtual", qualifiedByName = "bigDecimalToStringFourDecimalPlaces")
    @Mapping(target = "iofAtual", source = "iofAtual", qualifiedByName = "bigDecimalToStringFourDecimalPlaces")
    @Mapping(target = "valorLiquido", source = "valorLiquido", qualifiedByName = "bigDecimalToStringFourDecimalPlaces")
    @Mapping(target = "dataCriacao", source = "createdAt", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "dataAlteracao", source = "updatedAt", qualifiedByName = "localDateTimeToString")
    RendimentoResponseDTO toDto(RendimentoDiario entity);

}

