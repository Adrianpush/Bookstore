package com.school.bookstore.exceptions.users;

public class UserCreateException extends RuntimeException{

    public UserCreateException(String message) {
        super(message);
    }
}
