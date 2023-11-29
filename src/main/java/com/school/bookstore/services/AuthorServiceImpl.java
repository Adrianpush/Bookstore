package com.school.bookstore.services;

import com.school.bookstore.models.entities.Author;
import com.school.bookstore.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(String authorFullName) {
        Author author = new Author();
        author.setFullName(authorFullName);
        return authorRepository.save(author);
    }

    public Author getAuthorEntityById(Long id) {
        return authorRepository.findById(id).orElseThrow();
    }

    @Override
    public Optional<Author> getAuthorByName(String authorFullName) {
        return authorRepository.findByFullName(authorFullName);
    }
}
