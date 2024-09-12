package com.ucms.backend.enums;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ucms.backend.deserializer.GenericRoleDeserializer;
import lombok.Getter;

@Getter
@JsonDeserialize(using = GenericRoleDeserializer.class)
public enum Role {
    CUSTOMER("CUSTOMER"),
    SUPPLIER("SUPPLIER"),
    BOTH("BOTH");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toUpperCase();
    }
}
