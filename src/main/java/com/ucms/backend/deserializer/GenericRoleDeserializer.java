package com.ucms.backend.deserializer;

import com.ucms.backend.enums.Role;

public class GenericRoleDeserializer extends GenericEnumDeserializer<Role> {
    public GenericRoleDeserializer() {
        super(Role.class);
    }
}
