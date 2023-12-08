package com.school.bookstore.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    ObjectMapper objectMapper;

    public ExceptionHandlerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(UserCreateException.class)
    public ResponseEntity<String> userCreateException(UserCreateException userCreateException) {
        return new ResponseEntity<>(objectToString(
                Map.of("message", userCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(objectToString(
                Map.of("message", userNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(BookCreateException.class)
    public ResponseEntity<String> bookCreateException(BookCreateException bookCreateException) {
        return new ResponseEntity<>(objectToString(
                Map.of("message", bookCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> bookNotFoundException(BookNotFoundException bookNotFoundException) {
        return new ResponseEntity<>(objectToString(
                Map.of("message", bookNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<String> imageUploadException(ImageUploadException imageUploadException) {
        return new ResponseEntity<>(objectToString(Map.of("message", imageUploadException)), SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> authorNotFoundException(AuthorNotFoundException authorNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", authorNotFoundException)), NOT_FOUND);
    }

    @ExceptionHandler(OrderCreateException.class)
    public ResponseEntity<String> orderCreateException(OrderCreateException orderCreateException) {
        return new ResponseEntity<>(objectToString(Map.of("message", orderCreateException)), BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> orderNotFoundException(OrderCreateException orderNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", orderNotFoundException)), NOT_FOUND);
    }

    @ExceptionHandler(AuthentificationException.class)
    public ResponseEntity<String> authenticationException(AuthentificationException authentificationException) {
        return new ResponseEntity<>(objectToString(Map.of("message", authentificationException)), FORBIDDEN);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            String message = violation.getMessage();
            errors.put(fieldName, message);
        }
        return new ResponseEntity<>(objectToString(errors), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String defaultMessage = Objects.requireNonNull(error.getDefaultMessage());
            errors.put(error.getField(), defaultMessage);
        });
        return new ResponseEntity<>(objectToString(errors), HttpStatus.BAD_REQUEST);
    }

    private String objectToString(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            log.error("Error processing response to string");
            return "Internal error";
        }
    }
}
