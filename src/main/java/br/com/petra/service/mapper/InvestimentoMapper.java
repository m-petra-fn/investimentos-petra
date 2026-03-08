package br.com.petra.service.mapper;

import br.com.petra.domain.Investimento;
import br.com.petra.service.dto.InvestimentoRequestDTO;
import br.com.petra.service.dto.InvestimentoResponseDTO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvestimentoMapper {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "moeda", ignore = true)
    @Mapping(target = "tipoInvestimento", ignore = true)
    @Mapping(target = "tipoIndexacao", ignore = true)
    @Mapping(target = "valorInvestido", source = "valorInvestido", qualifiedByName = "stringToBigDecimal")
    Investimento toEntity(InvestimentoRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "moeda", ignore = true)
    @Mapping(target = "tipoInvestimento", ignore = true)
    @Mapping(target = "tipoIndexacao", ignore = true)
    @Mapping(target = "valorInvestido", source = "valorInvestido", qualifiedByName = "stringToBigDecimal")
    void updateEntityFromDto(InvestimentoRequestDTO dto, @MappingTarget Investimento entity);

    @Mapping(target = "valorInvestido", source = "valorInvestido", qualifiedByName = "bigDecimalToString")
    @Mapping(target = "moedaId", source = "moeda.id")
    @Mapping(target = "moedaCodigo", source = "moeda.codigo")
    @Mapping(target = "tipoInvestimentoId", source = "tipoInvestimento.id")
    @Mapping(target = "tipoInvestimentoCodigo", source = "tipoInvestimento.codigo")
    @Mapping(target = "tipoIndexacaoId", source = "tipoIndexacao.id")
    @Mapping(target = "tipoIndexacaoCodigo", source = "tipoIndexacao.codigo")
    @Mapping(target = "dataCriacao", source = "createdAt", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "dataAlteracao", source = "updatedAt", qualifiedByName = "localDateTimeToString")
    InvestimentoResponseDTO toDto(Investimento entity);

    @Named("stringToBigDecimal")
    default BigDecimal stringToBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @Named("bigDecimalToString")
    default String bigDecimalToString(BigDecimal value) {
        return value == null ? null : value.toPlainString();
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime value) {
        return value == null ? null : DATE_TIME_FORMATTER.format(value);
    }
}

