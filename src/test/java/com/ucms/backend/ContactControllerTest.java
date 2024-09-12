package com.ucms.backend;


import com.ucms.backend.controller.ContactController;
import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import com.ucms.backend.exception.ResourceNotFoundException;
import com.ucms.backend.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
    }

    @Test
    void testGetAllContacts() throws Exception {
        ContactDTO contact1 = new ContactDTO(1L, "John Doe", ContactType.PERSON, "john.doe@example.com", "1234567890", "123 Elm Street", Role.CUSTOMER);
        ContactDTO contact2 = new ContactDTO(2L, "ACME Corp.", ContactType.COMPANY, "info@acme.com", "0987654321", "456 Oak Avenue", Role.SUPPLIER);
        when(contactService.getAllContacts()).thenReturn(Arrays.asList(contact1, contact2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'contactId':1,'name':'John Doe','type':'PERSON','email':'john.doe@example.com','phone':'1234567890','address':'123 Elm Street','role':'CUSTOMER'},{'contactId':2,'name':'ACME Corp.','type':'COMPANY','email':'info@acme.com','phone':'0987654321','address':'456 Oak Avenue','role':'SUPPLIER'}]"));
    }

    @Test
    void testGetContactById() throws Exception {
        ContactDTO contact = new ContactDTO(1L, "John Doe", ContactType.PERSON, "john.doe@example.com", "1234567890", "123 Elm Street", Role.CUSTOMER);
        when(contactService.getContactById(1L)).thenReturn(Optional.of(contact));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'contactId':1,'name':'John Doe','type':'PERSON','email':'john.doe@example.com','phone':'1234567890','address':'123 Elm Street','role':'CUSTOMER'}"));
    }

    @Test
    void testGetContactByIdNotFound() throws Exception {
        when(contactService.getContactById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contacts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateContact() throws Exception {
        ContactDTO contact = new ContactDTO(1L, "John Doe", ContactType.PERSON, "john.doe@example.com", "1234567890", "123 Elm Street", Role.CUSTOMER);
        when(contactService.createContact(any(ContactDTO.class))).thenReturn(contact);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/contacts")
                        .contentType("application/json")
                        .content("{\"name\":\"John Doe\",\"type\":\"PERSON\",\"email\":\"john.doe@example.com\",\"phone\":\"1234567890\",\"address\":\"123 Elm Street\",\"role\":\"CUSTOMER\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'contactId':1,'name':'John Doe','type':'PERSON','email':'john.doe@example.com','phone':'1234567890','address':'123 Elm Street','role':'CUSTOMER'}"));
    }

    @Test
    void testUpdateContact() throws Exception {
        ContactDTO contact = new ContactDTO(1L, "John Doe", ContactType.PERSON, "john.doe@example.com", "1234567890", "123 Elm Street", Role.CUSTOMER);
        when(contactService.updateContact(eq(1L), any(ContactDTO.class))).thenReturn(contact);

        mockMvc.perform(put("/api/contacts/1")
                        .contentType("application/json")
                        .content("{\"name\":\"John Doe\",\"type\":\"PERSON\",\"email\":\"john.doe@example.com\",\"phone\":\"1234567890\",\"address\":\"123 Elm Street\",\"role\":\"CUSTOMER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'contactId':1,'name':'John Doe','type':'PERSON','email':'john.doe@example.com','phone':'1234567890','address':'123 Elm Street','role':'CUSTOMER'}"));
    }

    //@Test : TODO: fix this
    void testUpdateNonExistingContact() throws Exception {
        // Simulate the service throwing the ResourceNotFoundException
        when(contactService.updateContact(eq(1L), any(ContactDTO.class)))
                .thenThrow(new ResourceNotFoundException("Contact not found"));

        mockMvc.perform(put("/api/contacts/1")
                        .contentType("application/json")
                        .content("{\"name\":\"John Doe\",\"type\":\"PERSON\",\"email\":\"john.doe@example.com\",\"phone\":\"1234567890\",\"address\":\"123 Elm Street\",\"role\":\"CUSTOMER\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteContact() throws Exception {
        doNothing().when(contactService).deleteContact(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/contacts/1"))
                .andExpect(status().isNoContent());
    }
}
