package com.ucms.backend.frontend;

import com.ucms.backend.dto.ContactDTO;
import com.ucms.backend.enums.ContactType;
import com.ucms.backend.enums.Role;
import com.ucms.backend.response.PaginatedResponse;
import com.ucms.backend.service.ContactService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route("")
public class ContactView extends VerticalLayout {

    private final Grid<ContactDTO> grid = new Grid<>(ContactDTO.class);
    private final ContactService contactService;
    private final Binder<ContactDTO> binder = new Binder<>(ContactDTO.class);
    private Dialog contactFormDialog;
    private TextField searchField;
    // Pagination variables
    private int currentPage = 1; // Start with page 1
    private PaginatedResponse<ContactDTO> paginatedContacts;

    public ContactView(ContactService contactService) {
        this.contactService = contactService;

        configureSearchField();
        configureGrid();
        configureButtons();

        // Load initial page of contacts
        loadContacts();
        updateGridItems();

        // Add search field, grid, and pagination layout to the main layout
        add(searchField, grid, createPaginationLayout());
    }

    private void configureSearchField() {
        // Search bar for filtering contacts
        searchField = new TextField();
        searchField.setPlaceholder("Search by name or email...");
        searchField.setWidth("400px"); // Adjust width

        searchField.addValueChangeListener(e -> {
            currentPage = 1; // Reset to first page when searching
            loadContacts();
            updateGridItems();
        });
    }

    private void configureGrid() {
        grid.removeColumnByKey("contactId"); // Remove the ID column

        // Add Edit button column
        grid.addComponentColumn(contactDTO -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> openFormDialog(contactDTO, "Edit Contact"));
            return editButton;
        }).setHeader("Edit");

        // Add Delete button column with undo functionality
        grid.addComponentColumn(contactDTO -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                contactService.deleteContact(contactDTO.getContactId());
                loadContacts(); // Reload the contacts after deletion
                updateGridItems();
                Notification undoNotification = Notification.show("Contact deleted. Undo?", 5000, Notification.Position.MIDDLE);
                Button undoButton = new Button("Undo", clickEvent -> {
                    contactService.createContact(contactDTO);
                    loadContacts();
                    updateGridItems();
                    undoNotification.close();
                });
                undoNotification.add(undoButton);
            });
            return deleteButton;
        }).setHeader("Delete");

        // Enable multiple selection for batch actions
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void configureButtons() {
        // Add New Contact button
        Button addContactButton = new Button("Add New Contact", e -> openFormDialog(new ContactDTO(), "Add New Contact"));

        // Delete selected contacts button
        Button deleteSelectedButton = getDeleteSelectedButton();

        // Export to CSV button
        Button exportToCsvButton = new Button("Export to CSV", e -> exportContactsToCsv());

        // Theme switcher button
        Button themeSwitcher = new Button("Switch Theme", e -> {
            if (getElement().getThemeList().contains("dark")) {
                getElement().getThemeList().remove("dark");
            } else {
                getElement().getThemeList().add("dark");
            }
        });

        // Customize Columns button
        Button customizeColumnsButton = new Button("Customize Columns", e -> openCustomizeColumnsDialog());

        // Create a horizontal layout for the buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(
                addContactButton,
                deleteSelectedButton,
                exportToCsvButton,
                themeSwitcher,
                customizeColumnsButton // Added Customize Columns button
        );

        add(buttonLayout);
    }

    private Button getDeleteSelectedButton() {
        Button deleteSelectedButton = new Button("Delete Selected");
        deleteSelectedButton.addClickListener(e -> {
            Set<ContactDTO> selectedContacts = grid.getSelectedItems();
            selectedContacts.forEach(contact -> contactService.deleteContact(contact.getContactId()));
            loadContacts(); // Reload the contacts after deletion
            updateGridItems();
            Notification.show(selectedContacts.size() + " contacts deleted", 3000, Notification.Position.MIDDLE);
        });
        return deleteSelectedButton;
    }

    private HorizontalLayout createPaginationLayout() {
        Button previousPageButton = new Button("Previous Page", e -> {
            if (currentPage > 1) {
                currentPage--;
                loadContacts();
                updateGridItems();
            }
        });

        Button nextPageButton = new Button("Next Page", e -> {
            if (currentPage < paginatedContacts.getTotalPages()) {
                currentPage++;
                loadContacts();
                updateGridItems();
            }
        });

        return new HorizontalLayout(previousPageButton, nextPageButton);
    }

    private void loadContacts() {
        String searchTerm = searchField.getValue();
        int pageSize = 15;
        paginatedContacts = contactService.getAllContacts(currentPage, pageSize, searchTerm);
    }

    // Update grid items based on the current page
    private void updateGridItems() {
        grid.setItems(paginatedContacts.getContent());
    }

    private void openFormDialog(ContactDTO contact, String title) {
        contactFormDialog = new Dialog();
        contactFormDialog.setHeaderTitle(title);

        TextField nameField = new TextField("Name");
        binder.forField(nameField).bind(ContactDTO::getName, ContactDTO::setName);

        TextField phoneField = new TextField("Phone");
        binder.forField(phoneField)
                .withValidator(phone -> phone.matches("\\d{10}"), "Please enter a valid 10-digit phone number")
                .bind(ContactDTO::getPhone, ContactDTO::setPhone);

        EmailField emailField = new EmailField("Email");
        binder.forField(emailField)
                .withValidator(new EmailValidator("Please enter a valid email address"))
                .bind(ContactDTO::getEmail, ContactDTO::setEmail);

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

    private void openCustomizeColumnsDialog() {
        Dialog customizeDialog = new Dialog();

        Checkbox showNameColumn = new Checkbox("Show Name Column", true);
        showNameColumn.addValueChangeListener(event -> grid.getColumnByKey("name").setVisible(event.getValue()));

        Checkbox showEmailColumn = new Checkbox("Show Email Column", true);
        showEmailColumn.addValueChangeListener(event -> grid.getColumnByKey("email").setVisible(event.getValue()));

        customizeDialog.add(new VerticalLayout(showNameColumn, showEmailColumn));
        customizeDialog.open();
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
                loadContacts(); // Reload contacts
                updateGridItems();
            } else {
                Notification.show("Form is invalid, please check your input.", 3000, Notification.Position.MIDDLE);
            }
        });

        Button cancelButton = new Button("Cancel", e -> contactFormDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        return new VerticalLayout(formLayout, buttonsLayout);
    }

    private void exportContactsToCsv() {
        List<ContactDTO> contacts = paginatedContacts.getContent();
        String csvContent = contacts.stream()
                .map(contact -> String.join(",", contact.getName(), contact.getEmail(), contact.getPhone(), contact.getAddress()))
                .collect(Collectors.joining("\n"));

        // Create a StreamResource for the CSV file
        StreamResource resource = new StreamResource("contacts.csv",
                () -> new ByteArrayInputStream(csvContent.getBytes()));

        // Create an Anchor for the download
        Anchor downloadLink = new Anchor(resource, "Download Contacts CSV");
        downloadLink.getElement().setAttribute("download", true);

        // Add the download link to the UI
        add(downloadLink);

        // Use JavaScript to simulate a click on the link
        downloadLink.getElement().callJsFunction("click");

        // Remove the link from the UI after the download has started
        remove(downloadLink);
    }

}
