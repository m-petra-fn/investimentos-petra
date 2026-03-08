package br.com.petra.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

@Converter(autoApply = false)
public class StringTrimConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return StringUtils.trim(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return StringUtils.trim(dbData);
    }
}
