package com.school.bookstore.exceptions;

public class CustomerCreateException extends RuntimeException{

    public CustomerCreateException(String message) {
        super(message);
    }
}
