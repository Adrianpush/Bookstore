package com.school.bookstore.exceptions.book;

public class InvalidLanguageException extends RuntimeException {

    public InvalidLanguageException(String message) {
        super(message);
    }
}
