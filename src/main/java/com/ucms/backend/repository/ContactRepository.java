package com.ucms.backend.repository;

import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import com.ucms.backend.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmailAndTypeAndRole(String email, ContactType typ, Role role);

    @Query("SELECT c FROM Contact c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.type) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.role) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Contact> searchByMultipleFields(@Param("searchTerm") String searchTerm, Pageable pageable);

}

