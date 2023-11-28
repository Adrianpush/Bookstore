package com.school.bookstore.services;

import com.school.bookstore.models.entities.Author;

import java.util.Optional;

public interface AuthorService {
    Author createAuthor(String authorFullName);
    Optional<Author> getAuthorByName(String authorFullName);

}
