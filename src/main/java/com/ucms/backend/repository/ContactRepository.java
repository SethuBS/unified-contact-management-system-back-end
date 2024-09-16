package com.ucms.backend.repository;

import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import com.ucms.backend.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmailAndTypeAndRole(String email, ContactType typ, Role role);
}

