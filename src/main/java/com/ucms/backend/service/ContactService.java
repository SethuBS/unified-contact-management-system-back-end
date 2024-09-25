package com.ucms.backend.service;


import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.response.PaginatedResponse;

import java.util.Optional;

public interface ContactService {
    PaginatedResponse<ContactDTO> getAllContacts(int page, int size, String search);

    Optional<ContactDTO> getContactById(Long id);

    ContactDTO createContact(ContactDTO contactDTO);

    ContactDTO updateContact(Long id, ContactDTO contactDTO);

    void deleteContact(Long id);
}
