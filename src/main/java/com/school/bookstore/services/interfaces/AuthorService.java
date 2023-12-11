package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.models.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author createAuthor(String authorFullName);

    Optional<Author> getAuthorByName(String authorFullName);

    AuthorDTO getAuthorById(Long id);

    List<AuthorDTO> getAllAuthors();

    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);

    void deleteAuthor(Author author);
}
