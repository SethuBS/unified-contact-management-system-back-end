package com.ucms.backend.service;


import com.ucms.backend.dto.ContactDTO;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    List<ContactDTO> getAllContacts();

    Optional<ContactDTO> getContactById(Long id);

    ContactDTO createContact(ContactDTO contactDTO);

    ContactDTO updateContact(Long id, ContactDTO contactDTO);

    void deleteContact(Long id);
}
