package com.ucms.backend.deserializer;

import com.ucms.backend.enums.ContactType;

public class GenericContactTypeDeserializer extends GenericEnumDeserializer<ContactType> {
    public GenericContactTypeDeserializer() {
        super(ContactType.class);
    }
}
