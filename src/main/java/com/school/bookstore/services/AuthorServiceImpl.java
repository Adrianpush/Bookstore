package com.school.bookstore.services;

import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.models.entities.Author;
import com.school.bookstore.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setFullName(authorDTO.getFullName());
        author.setAuthorInformation(authorDTO.getAuthorInformation());
        author = authorRepository.save(author);
        AuthorDTO authorResponseDTO = new AuthorDTO();
        authorResponseDTO.setId(author.getId());
        authorResponseDTO.setFullName(author.getFullName());
        authorResponseDTO.setAuthorInformation(author.getAuthorInformation());

        return authorResponseDTO;
    }

    @Override
    public AuthorDTO createAuthor(String authorFullName) {
        Author author = new Author();
        author.setFullName(authorFullName);
        author = authorRepository.save(author);
        AuthorDTO authorResponseDTO = new AuthorDTO();
        authorResponseDTO.setId(author.getId());
        authorResponseDTO.setFullName(author.getFullName());
        authorResponseDTO.setAuthorInformation(author.getAuthorInformation());

        return authorResponseDTO;
    }
}
