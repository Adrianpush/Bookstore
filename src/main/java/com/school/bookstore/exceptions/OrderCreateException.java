package com.school.bookstore.exceptions;

public class OrderCreateException extends RuntimeException {

    public OrderCreateException(String message) {
        super(message);
    }
}
