package com.school.bookstore.exceptions.order;

public class OrderCreateException extends RuntimeException {

    public OrderCreateException(String message) {
        super(message);
    }
}
