package com.ucms.backend.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    private void getServletRequestAttributesAndSetPath(ExceptionDetails error) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String path = request.getRequestURI();
            error.setPath(path);
        }
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException e) {
        ExceptionDetails error = new ExceptionDetails();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError(HttpStatus.NOT_FOUND.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceFoundException(ResourceFoundException e) {
        ExceptionDetails error = new ExceptionDetails();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.FOUND.value());
        error.setError(HttpStatus.FOUND.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.FOUND).body(error);
    }

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ExceptionDetails> handleInvalidEnumValueException(InvalidEnumValueException e) {
        ExceptionDetails error = new ExceptionDetails();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidRoleUpdateException.class)
    public ResponseEntity<ExceptionDetails> handleInvalidRoleUpdateException(InvalidRoleUpdateException e) {
        ExceptionDetails error = new ExceptionDetails();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ContactIllegalArgumentException.class)
    public ResponseEntity<ExceptionDetails> handleContactIllegalArgumentException(ContactIllegalArgumentException e) {
        ExceptionDetails error = new ExceptionDetails();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
