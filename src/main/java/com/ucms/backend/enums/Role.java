package com.ucms.backend.enums;

import com.ucms.backend.deserializer.EnumValue;
import lombok.Getter;

@Getter
public enum Role implements EnumValue {
    CUSTOMER("CUSTOMER"),
    SUPPLIER("SUPPLIER"),
    BOTH("BOTH");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return "";
    }
}
