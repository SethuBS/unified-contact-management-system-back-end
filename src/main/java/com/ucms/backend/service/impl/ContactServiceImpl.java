package com.ucms.backend.service.impl;


import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import com.ucms.backend.exception.ContactIllegalArgumentException;
import com.ucms.backend.exception.InvalidRoleUpdateException;
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
        validateContactTypeAndRole(contactDTO.getType(), contactDTO.getRole());
        contactRepository.findByEmailAndRole(contactDTO.getEmail(), contactDTO.getRole())
                .ifPresent(contact -> {
                    throw new ResourceFoundException("Contact with email: " + contactDTO.getEmail() + " is already in the system  as " + contactDTO.getRole());
                });
        var contact = ContactMapper.contactDTOToContact(contactDTO);
        var savedContact = contactRepository.save(contact);
        return ContactMapper.contactToContactDTO(savedContact);
    }

    @Override
    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        return contactRepository.findById(id)
                .map(existingContact -> {
                    // Validate the ContactType and Role combination
                    validateContactTypeAndRole(contactDTO.getType(), contactDTO.getRole());

                    // Check if the role update is allowed based on the current role
                    if (!canAddOrUpdateRole(existingContact, contactDTO)) {
                        throw new InvalidRoleUpdateException("Cannot add or update role as the contact already has all roles.");
                    }

                    // Update fields
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

    private void validateContactTypeAndRole(ContactType type, Role role) {
        // Ensure that customers and suppliers can either be a person or a company
        if (type == null || role == null) {
            throw new ContactIllegalArgumentException("Contact type and role must be provided.");
        }

        // Enforcing the rule that a contact must be either a person or company
        if (!(type == ContactType.PERSON || type == ContactType.COMPANY)) {
            throw new ContactIllegalArgumentException("Invalid contact type: A contact can only be a person or a company.");
        }

        // Optionally, you can add other rules here based on role (e.g., BOTH role also must be PERSON or COMPANY)
        if (role == Role.BOTH && !(type == ContactType.PERSON || type == ContactType.COMPANY)) {
            throw new ContactIllegalArgumentException("Invalid role: A contact with both roles must be either a person or a company.");
        }
    }

    private boolean canAddOrUpdateRole(Contact existingContact, ContactDTO contactDTO) {
        // Prevent updating role if the contact already has both CUSTOMER and SUPPLIER roles
        return !(existingContact.getRole() == Role.BOTH && contactDTO.getRole() != existingContact.getRole());
    }
}
