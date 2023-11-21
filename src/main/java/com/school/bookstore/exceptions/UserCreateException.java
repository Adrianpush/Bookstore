package com.school.bookstore.exceptions;

public class UserCreateException extends RuntimeException{

    public UserCreateException(String message) {
        super(message);
    }
}
