package com.ucms.backend.enums;

import com.ucms.backend.deserializer.EnumValue;
import lombok.Getter;

@Getter
public enum ContactType implements EnumValue {
    PERSON("PERSON"),
    COMPANY("COMPANY");

    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
