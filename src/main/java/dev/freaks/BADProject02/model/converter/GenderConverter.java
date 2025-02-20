package dev.freaks.BADProject02.model.converter;

import dev.freaks.BADProject02.model.constant.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class GenderConverter implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        // Remove any quotes that might come from ENUM type
        String cleanValue = dbData.replace("'", "").trim();

        for (Gender gender : Gender.values()) {
            if (gender.getValue().equals(cleanValue)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender value: " + dbData);
    }
}