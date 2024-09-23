package com.ucms.backend.frontend;

import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import com.ucms.backend.service.ContactService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

@Route("")
public class ContactView extends VerticalLayout {

    private final Grid<ContactDTO> grid = new Grid<>(ContactDTO.class);
    private final ContactService contactService;
    private Dialog contactFormDialog;
    private final Binder<ContactDTO> binder = new Binder<>(ContactDTO.class);

    public ContactView(ContactService contactService) {
        this.contactService = contactService;

        grid.setItems(contactService.getAllContacts());
        grid.removeColumnByKey("contactId"); // Remove the ID column

        // Add Edit button column
        grid.addComponentColumn(contactDTO -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> openFormDialog(contactDTO, "Edit Contact"));
            return editButton;
        }).setHeader("Edit");

        // Add Delete button column
        grid.addComponentColumn(contactDTO -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                contactService.deleteContact(contactDTO.getContactId());
                grid.setItems(contactService.getAllContacts()); // Refresh grid
                Notification.show("Contact deleted", 3000, Notification.Position.MIDDLE);
            });
            return deleteButton;
        }).setHeader("Delete");

        // Button to add new contact
        Button addContactButton = new Button("Add New Contact", e -> openFormDialog(new ContactDTO(), "Add New Contact"));

        add(addContactButton, grid);
    }

    // Open the form dialog for adding or editing a contact
    private void openFormDialog(ContactDTO contact, String title) {
        contactFormDialog = new Dialog();
        contactFormDialog.setHeaderTitle(title);

        TextField nameField = new TextField("Name");
        binder.forField(nameField).bind(ContactDTO::getName, ContactDTO::setName);

        TextField phoneField = new TextField("Phone");
        binder.forField(phoneField).bind(ContactDTO::getPhone, ContactDTO::setPhone);

        EmailField emailField = new EmailField("Email");
        binder.forField(emailField).bind(ContactDTO::getEmail, ContactDTO::setEmail);

        TextField addressField = new TextField("Address");
        binder.forField(addressField).bind(ContactDTO::getAddress, ContactDTO::setAddress);

        ComboBox<ContactType> typeField = new ComboBox<>("Contact Type", ContactType.values());
        binder.forField(typeField).bind(ContactDTO::getType, ContactDTO::setType);

        ComboBox<Role> roleField = new ComboBox<>("Role", Role.values());
        binder.forField(roleField).bind(ContactDTO::getRole, ContactDTO::setRole);

        binder.setBean(contact); // Set the binder with the contact data

        FormLayout formLayout = new FormLayout(nameField, phoneField, emailField, addressField, typeField, roleField);

        VerticalLayout dialogContent = getVerticalLayout(contact, formLayout);

        contactFormDialog.add(dialogContent);
        contactFormDialog.open();
    }

    private VerticalLayout getVerticalLayout(ContactDTO contact, FormLayout formLayout) {
        Button saveButton = new Button("Save", e -> {
            if (binder.writeBeanIfValid(contact)) {
                if (contact.getContactId() == null) {
                    // Add new contact
                    contactService.createContact(contact);
                    Notification.show("New contact added", 3000, Notification.Position.MIDDLE);
                } else {
                    // Update existing contact
                    contactService.updateContact(contact.getContactId(), contact);
                    Notification.show("Contact updated", 3000, Notification.Position.MIDDLE);
                }
                contactFormDialog.close();
                grid.setItems(contactService.getAllContacts()); // Refresh grid
            } else {
                Notification.show("Form is invalid, please check your input.", 3000, Notification.Position.MIDDLE);
            }
        });

        Button cancelButton = new Button("Cancel", e -> contactFormDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        return new VerticalLayout(formLayout, buttonsLayout);
    }
}
