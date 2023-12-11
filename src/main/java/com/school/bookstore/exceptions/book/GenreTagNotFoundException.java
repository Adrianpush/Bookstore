package com.school.bookstore.exceptions.book;

public class GenreTagNotFoundException extends RuntimeException {

    public GenreTagNotFoundException(String message) {
        super(message);
    }
}
