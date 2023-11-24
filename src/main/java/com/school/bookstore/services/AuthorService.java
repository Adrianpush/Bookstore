package com.school.bookstore.services;

import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.models.entities.Author;

public interface AuthorService {

    AuthorDTO createAuthor(AuthorDTO authorDTO);
    AuthorDTO createAuthor(String authorFullName);
}
