package com.ucms.backend.model;

import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    private String name;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    private String email;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
}
