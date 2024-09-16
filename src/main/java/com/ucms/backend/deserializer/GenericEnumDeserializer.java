package com.ucms.backend.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ucms.backend.exception.ContactIllegalArgumentException;

import java.io.IOException;

public class GenericEnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

    private final Class<T> enumClass;

    public GenericEnumDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();

        // Iterate through the enum constants
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return enumConstant;  // Return matching enum constant
            }
        }

        if (value.isBlank() || value.isEmpty()) {
            throw new ContactIllegalArgumentException(enumClass.getSimpleName() + " cannot be null or empty.");
        }

        // If no match found, throw a custom exception (you can define this exception as needed)
        throw new ContactIllegalArgumentException("Invalid value: " + value + " for enum: " + enumClass.getSimpleName());
    }
}