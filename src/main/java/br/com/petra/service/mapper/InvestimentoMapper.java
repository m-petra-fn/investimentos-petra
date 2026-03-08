package br.com.petra.service.mapper;

import br.com.petra.domain.Investimento;
import br.com.petra.service.dto.InvestimentoRequestDTO;
import br.com.petra.service.dto.InvestimentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvestimentoMapper extends AbstractMapper {

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

    @Mapping(target = "valorInvestido", source = "valorInvestido", qualifiedByName = "bigDecimalToStringFourDecimalPlaces")
    @Mapping(target = "moeda", source = "moeda.codigo")
    @Mapping(target = "cpfInvestidor", source = "cpfInvestidor", qualifiedByName = "leftPadCpf")
    @Mapping(target = "tipoInvestimento", source = "tipoInvestimento.codigo")
    @Mapping(target = "tipoIndexacao", source = "tipoIndexacao.codigo")
    @Mapping(target = "dataCriacao", source = "createdAt", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "dataAlteracao", source = "updatedAt", qualifiedByName = "localDateTimeToString")
    InvestimentoResponseDTO toDto(Investimento entity);

}

