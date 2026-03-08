package br.com.petra.service.mapper;

import br.com.petra.domain.RendimentoDiario;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RendimentoMapper {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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
    @Mapping(target = "valorRendido", source = "valorRendido", qualifiedByName = "bigDecimalToString")
    @Mapping(target = "irAtual", source = "irAtual", qualifiedByName = "bigDecimalToString")
    @Mapping(target = "iofAtual", source = "iofAtual", qualifiedByName = "bigDecimalToString")
    @Mapping(target = "valorLiquido", source = "valorLiquido", qualifiedByName = "bigDecimalToString")
    @Mapping(target = "dataCriacao", source = "createdAt", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "dataAlteracao", source = "updatedAt", qualifiedByName = "localDateTimeToString")
    RendimentoResponseDTO toDto(RendimentoDiario entity);

    @Named("stringToBigDecimal")
    default BigDecimal stringToBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @Named("bigDecimalToString")
    default String bigDecimalToString(BigDecimal value) {
        return value == null ? null : value.toPlainString();
    }

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String value) {
        return value == null ? null : LocalDate.parse(value, DATE_FORMATTER);
    }

    @Named("localDateToString")
    default String localDateToString(LocalDate value) {
        return value == null ? null : DATE_FORMATTER.format(value);
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime value) {
        return value == null ? null : DATE_TIME_FORMATTER.format(value);
    }
}

