package br.com.petra.service.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

abstract class AbstractDTOMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;


    void formatDecimals(Consumer<String> setter, Supplier<BigDecimal> getter) {
        if (getter.get() != null) {
            setter.accept(formatBigDecimal(getter.get()));
        }
    }

    void formatDecimals(Pair<Consumer<String>, Supplier<BigDecimal>> pair) {
        formatDecimals(pair.getFirst(), pair.getSecond());
    }

    void formatDecimals(Collection<Pair<Consumer<String>, Supplier<BigDecimal>>> lista) {
        lista.forEach(this::formatDecimals);
    }

    String formatBigDecimal(BigDecimal value) {
        return value == null ? null : value.setScale(4, RoundingMode.HALF_EVEN).toPlainString();
    }

    String formatDateTime(LocalDateTime value) {
        return value == null ? null : DATE_TIME_FORMATTER.format(value);
    }

    String leftPadCpf(String value) {
        return StringUtils.leftPad(value, 4, '0');
    }

    String formatDate(LocalDate value) {
        return value == null ? null : DATE_FORMATTER.format(value);
    }


}
