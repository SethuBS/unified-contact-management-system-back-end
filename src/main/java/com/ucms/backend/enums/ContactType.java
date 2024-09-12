package com.ucms.backend.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ucms.backend.deserializer.GenericContactTypeDeserializer;
import lombok.Getter;

@Getter
@JsonDeserialize(using = GenericContactTypeDeserializer.class)
public enum ContactType {
    PERSON("PERSON"),
    COMPANY("COMPANY");

    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toUpperCase();
    }
}
