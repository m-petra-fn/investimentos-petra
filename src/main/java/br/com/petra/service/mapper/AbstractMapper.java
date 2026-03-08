package br.com.petra.service.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AbstractMapper {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Named("stringToBigDecimal")
    default BigDecimal stringToBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @Named("bigDecimalToString")
    default String bigDecimalToString(BigDecimal value) {
        return value == null ? null : value.toPlainString();
    }

    @Named("bigDecimalToStringFourDecimalPlaces")
    default String bigDecimalToStringFourDecimalPlaces(BigDecimal value) {
        if (value == null) {
            return null;
        }

        return value.setScale(4, RoundingMode.HALF_EVEN).toPlainString();
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime value) {
        return value == null ? null : DATE_TIME_FORMATTER.format(value);
    }

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String value) {
        return value == null ? null : LocalDate.parse(value, DATE_FORMATTER);
    }

    @Named("localDateToString")
    default String localDateToString(LocalDate value) {
        return value == null ? null : DATE_FORMATTER.format(value);
    }

    @Named("leftPadCpf")
    default String leftPadCpf(String value) {
        return StringUtils.leftPad(value, 11, '0');
    }

}

