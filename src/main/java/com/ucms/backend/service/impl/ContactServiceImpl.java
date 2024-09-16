package com.ucms.backend.service.impl;


import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.exception.ResourceFoundException;
import com.ucms.backend.exception.ResourceNotFoundException;
import com.ucms.backend.map.ContactMapper;
import com.ucms.backend.model.Contact;
import com.ucms.backend.repository.ContactRepository;
import com.ucms.backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(ContactMapper::contactToContactDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContactDTO> getContactById(Long id) {
        return contactRepository.findById(id)
                .map(ContactMapper::contactToContactDTO);
    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        contactRepository.findByEmailAndTypeAndRole(contactDTO.getEmail(), contactDTO.getType(), contactDTO.getRole())
                .ifPresent(contact -> {
                    throw new ResourceFoundException(
                            "A contact with email '" + contactDTO.getEmail() + "' has already been added to the system as a "
                                    + contactDTO.getType() + " with the role of " + contactDTO.getRole() + ". "
                                    + "You are not allowed to create a new contact using this email address. "
                                    + "However, you can update the existing contact to change its type or role."
                    );
                });
        var contact = ContactMapper.contactDTOToContact(contactDTO);
        var savedContact = contactRepository.save(contact);
        return ContactMapper.contactToContactDTO(savedContact);
    }

    @Override
    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        return contactRepository.findById(id)
                .map(existingContact -> {
                    existingContact.setName(contactDTO.getName());
                    existingContact.setType(contactDTO.getType());
                    existingContact.setEmail(contactDTO.getEmail());
                    existingContact.setPhone(contactDTO.getPhone());
                    existingContact.setAddress(contactDTO.getAddress());
                    existingContact.setRole(contactDTO.getRole());
                    Contact updatedContact = contactRepository.save(existingContact);
                    return ContactMapper.contactToContactDTO(updatedContact);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
