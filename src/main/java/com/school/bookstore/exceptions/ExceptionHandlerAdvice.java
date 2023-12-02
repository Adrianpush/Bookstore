package com.school.bookstore.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

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

    @ExceptionHandler(CustomerCreateException.class)
    public ResponseEntity<String> customerCreateException(CustomerCreateException customerCreateException) {
        return  new ResponseEntity<>(objectToString(Map.of("message", customerCreateException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> customerNotFoundException(CustomerNotFoundException customerNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", customerNotFoundException)), NOT_FOUND);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<String> imageUploadException(ImageUploadException imageUploadException) {
        return new ResponseEntity<>(objectToString(Map.of("message", imageUploadException)), SERVICE_UNAVAILABLE);
    }

    private String objectToString(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Error processing response to string");
            return "Internal error";
        }
    }
}
