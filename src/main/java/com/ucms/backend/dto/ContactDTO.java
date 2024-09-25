package com.ucms.backend.dto;

import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactDTO {
    private Long contactId;
    private String name;
    private ContactType type;
    private String email;
    private String phone;
    private String address;
    private Role role;
    private LocalDate createdAt = LocalDate.now();
}
