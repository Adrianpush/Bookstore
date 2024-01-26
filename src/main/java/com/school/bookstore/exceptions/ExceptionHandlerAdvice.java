package com.school.bookstore.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.bookstore.exceptions.book.*;
import com.school.bookstore.exceptions.order.OrderCreateException;
import com.school.bookstore.exceptions.order.OrderNotFoundException;
import com.school.bookstore.exceptions.users.AuthentificationException;
import com.school.bookstore.exceptions.users.AuthorNotFoundException;
import com.school.bookstore.exceptions.users.UserCreateException;
import com.school.bookstore.exceptions.users.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String MESSAGE = "message";
    @Autowired
    ObjectMapper objectMapper;

    public ExceptionHandlerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(UserCreateException.class)
    public ResponseEntity<String> userCreateException(UserCreateException userCreateException) {
        return new ResponseEntity<>(objectToString(
                Map.of(MESSAGE, userCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(objectToString(
                Map.of(MESSAGE, userNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(BookCreateException.class)
    public ResponseEntity<String> bookCreateException(BookCreateException bookCreateException) {
        return new ResponseEntity<>(objectToString(
                Map.of(MESSAGE, bookCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(BookDeleteException.class)
    public ResponseEntity<String> bookDeleteException(BookDeleteException bookDeleteException) {
        return new ResponseEntity<>(objectToString(
                Map.of(MESSAGE, bookDeleteException.getMessage())), CONFLICT);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> bookNotFoundException(BookNotFoundException bookNotFoundException) {
        return new ResponseEntity<>(objectToString(
                Map.of(MESSAGE, bookNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(InvalidLanguageException.class)
    public ResponseEntity<String> invalidLanguageException(InvalidLanguageException invalidLanguageException) {
        return new ResponseEntity<>(objectToString(
                Map.of(MESSAGE, invalidLanguageException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<String> invalidImageException(InvalidImageException invalidImageException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, invalidImageException)), BAD_REQUEST);
    }

    @ExceptionHandler(ImageStorageException.class)
    public ResponseEntity<String> imageUploadException(ImageStorageException imageStorageException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, imageStorageException)), SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> authorNotFoundException(AuthorNotFoundException authorNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, authorNotFoundException)), NOT_FOUND);
    }

    @ExceptionHandler(GenreTagNotFoundException.class)
    public ResponseEntity<String> genreTagNotFoundException(GenreTagNotFoundException genreTagNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, genreTagNotFoundException)), NOT_FOUND);
    }

    @ExceptionHandler(OrderCreateException.class)
    public ResponseEntity<String> orderCreateException(OrderCreateException orderCreateException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, orderCreateException)), BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> orderNotFoundException(OrderCreateException orderNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, orderNotFoundException)), NOT_FOUND);
    }

    @ExceptionHandler(AuthentificationException.class)
    public ResponseEntity<String> authenticationException(AuthentificationException authentificationException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, authentificationException)), FORBIDDEN);
    }

    @ExceptionHandler(RecommendationException.class)
    public ResponseEntity<String> recommendationException(RecommendationException recommendationException) {
        return new ResponseEntity<>(objectToString(Map.of(MESSAGE, recommendationException)), SERVICE_UNAVAILABLE);
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
