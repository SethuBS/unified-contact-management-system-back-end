package com.ucms.backend.map;

import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.model.Contact;


public class ContactMapper {

    public static ContactDTO contactToContactDTO(Contact contact) {
        if (contact == null)
            return null;

        return ContactDTO.builder()
                .contactId(contact.getContactId())
                .name(contact.getName())
                .type(contact.getType())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .address(contact.getAddress())
                .role(contact.getRole())
                .createdAt(contact.getCreatedAt())
                .build();

    }

    public static Contact contactDTOToContact(ContactDTO contactDTO) {
        if (contactDTO == null)
            return null;

        return Contact.builder()
                .contactId(contactDTO.getContactId())
                .name(contactDTO.getName())
                .type(contactDTO.getType())
                .email(contactDTO.getEmail())
                .phone(contactDTO.getPhone())
                .address(contactDTO.getAddress())
                .role(contactDTO.getRole())
                .createdAt(contactDTO.getCreatedAt())
                .build();
    }
}
