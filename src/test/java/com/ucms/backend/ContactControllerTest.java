package com.ucms.backend;


import com.ucms.backend.controller.ContactController;
import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import com.ucms.backend.response.PaginatedResponse;
import com.ucms.backend.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testFindAllContacts_WithPaginationAndSearch() {
        // Given
        int page = 1;
        int size = 20;
        String search = "John";

        // Sample data
        ContactDTO contact1 = new ContactDTO(1L, "John Doe", ContactType.PERSON, "john@example.com", "1234567890", "123 Elm St", Role.CUSTOMER);
        ContactDTO contact2 = new ContactDTO(2L, "Jane Doe", ContactType.COMPANY, "jane@example.com", "0987654321", "456 Oak St", Role.SUPPLIER);
        List<ContactDTO> contacts = Arrays.asList(contact1, contact2);
        PaginatedResponse<ContactDTO> paginatedResponse = new PaginatedResponse<>(contacts, 1, 2, page, size);

        when(contactService.getAllContacts(page, size, search)).thenReturn(paginatedResponse);

        // When
        ResponseEntity<PaginatedResponse<ContactDTO>> responseEntity = contactController.getAllContacts(page, size, search);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(paginatedResponse, responseEntity.getBody());

        // Verify that the service method was called with the correct parameters
        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> searchCaptor = ArgumentCaptor.forClass(String.class);
        verify(contactService).getAllContacts(pageCaptor.capture(), sizeCaptor.capture(), searchCaptor.capture());

        assertEquals(page, pageCaptor.getValue());
        assertEquals(size, sizeCaptor.getValue());
        assertEquals(search, searchCaptor.getValue());
    }

    @Test
    void testFindAllContacts_NoSearch() {
        // Given
        int page = 1;
        int size = 20;

        // Sample data
        ContactDTO contact1 = new ContactDTO(1L, "John Doe", ContactType.PERSON, "john@example.com", "1234567890", "123 Elm St", Role.BOTH);
        List<ContactDTO> contacts = List.of(contact1);
        PaginatedResponse<ContactDTO> paginatedResponse = new PaginatedResponse<>(contacts, 1, 1, page, size);

        when(contactService.getAllContacts(page, size, null)).thenReturn(paginatedResponse);

        // When
        ResponseEntity<PaginatedResponse<ContactDTO>> responseEntity = contactController.getAllContacts(page, size, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(paginatedResponse, responseEntity.getBody());

        // Verify that the service method was called with the correct parameters
        verify(contactService).getAllContacts(page, size, null);
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

    @Test
    void testDeleteContact() throws Exception {
        doNothing().when(contactService).deleteContact(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/contacts/1"))
                .andExpect(status().isNoContent());
    }
}
