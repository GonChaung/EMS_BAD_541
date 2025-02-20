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

        for (Gender gender : Gender.values()) {
            if (gender.value.equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender value: " + value);
    }
}