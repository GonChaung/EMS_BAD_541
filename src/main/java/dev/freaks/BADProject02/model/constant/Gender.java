package dev.freaks.BADProject02.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Gender {
    MALE("M"),
    FEMALE("F");

    private final String value;

    // This will be used when persisting to the database
    @Override
    public String toString() {
        return this.value;
    }

    // This will be used when reading from the database
    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }

        value = value.trim().toUpperCase(); // Normalize input

        switch (value) {
            case "M":
            case "MALE":
                return MALE;
            case "F":
            case "FEMALE":
                return FEMALE;
            default:
                throw new IllegalArgumentException("Unknown gender value: " + value);
        }
    }
}