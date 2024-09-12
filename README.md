# Project Description

The ***Unified  Contact Management System*** is designed to manage and handle information about various contacts, which can be individuals or companies. The system uses a unified entity model to store both customer and supplier details within a single table. This approach simplifies data management but may have limitations when it comes to handling future changes or adding unique attributes for specific roles.

## Key Features
1. **Unified Entity Model:**
   - ***Contact Entity:*** Stores common attributes like name, email, phone, and address, and includes a `Role` attribute to specify whether a contact is a customer, supplier, or both.
   - ***Role Enum:*** Defines roles as 'Customer', 'Supplier', or 'Both' to capture the multiple roles a contact can have.
  
2. **CRUD Operations:**
   - ***Create***: Add new contacts to the system.
   - ***Read***: Retrieve contact details by ID or list all contacts.
   - ***Update***: Modify existing contact details.
   - ***Delete***: Remove contacts from the system.
  
3. **API Endpoints:**
   - `GET /api/contacts`: Fetches a list of all contacts.
   - `GET /api/contacts/{id}`: Retrieves a specific contact by ID.
   - `POST /api/contacts`: Adds a new contact.
   - `PUT /api/contacts/{id}`: Updates an existing contact.
   - `DELETE /api/contacts/{id}`: Deletes a contact by ID.
  
4. **Data Integrity:**
   - Validations and constraints ensure the accuracy and consistency of the data.
  
5. **DTO and Mapper:**
   - ***DTOs:*** `ContactDTO` is used to transfer data between the API and service layers.
   - ***Mapper:*** `ContactMappe`r uses MapStruct to handle conversions between Contact entities and ContactDTO objects.
  
6. **Service Layer:**
   - ***Service Interface:*** `ContactService` defines the methods for CRUD operations.
   - ***Service Implementation:*** `ContactServiceImpl` implements the interface, utilizing the ContactMapper for entity-DTO conversions.
  
7. **Exception Handling:**
   - ***Custom Exceptions:*** `ResourceNotFoundException` handles scenarios where a contact is not found.
  
# Project Package Structure
Here’s the updated package structure:
```graphql
com.example.contactapp
│
├── ContactAppApplication.java   # Main application class
│
├── controller
│   └── ContactController.java   # Handles HTTP requests and responses
│
├── dto
│   └── ContactDTO.java          # Data Transfer Object for Contact
│
├── model
│   └── Contact.java             # Represents the Contact entity
│
├── repository
│   └── ContactRepository.java   # Interface for CRUD operations with the database
│
├── service
│   ├── ContactService.java      # Service interface for business logic
│   └── impl
│       └── ContactServiceImpl.java # Implementation of the service interface
│
├── mapper
│   └── ContactMapper.java       # Interface for mapping between DTO and entity
│
└── exception
    └── ResourceNotFoundException.java # Custom exception for resource not found scenarios
```

# Detailed Explanation

1. **Main Application Class** (`ContactAppApplication.java`):
   - Initializes the Spring Boot application and starts the server.
  
2. **Controller** (`ContactController.java`):
   - Defines REST API endpoints for performing CRUD operations on contacts.
   - Uses `@RestController` to map HTTP requests to service methods.
     
3. **DTO** (`ContactDTO.java`):
   - Represents the data structure used to transfer contact information between the API and service layers.
   - 
4. **Model** (`Contact.java`):
   - Defines the `Contact` entity with attributes like `contactId`, `name`, `type`, `email`, `phone`, `address`, and `role`.

5. **Repository** (`ContactRepository.java`):
   - Provides CRUD operations for the Contact entity using Spring Data JPA.
     
6. **Service** (`ContactService.java` and `ContactServiceImpl.java`):
   - `ContactService`: Interface declaring CRUD methods.
   - `ContactServiceImp`l: Implementation class that contains business logic and interacts with the ContactRepository.
     
7. **Mapper** (`ContactMapper.java`):
   - Uses MapStruct to map between Contact entities and ContactDTO objects.
     
8. **Exception Handling** (`ResourceNotFoundException.java`):
   - Custom exception class used to handle cases where a contact is not found in the database.
     
This structure ensures a well-organized, maintainable codebase with clear separation of concerns, facilitating easier development and management of the Contact Management System.

