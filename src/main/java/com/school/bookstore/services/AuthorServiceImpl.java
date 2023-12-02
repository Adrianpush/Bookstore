package com.school.bookstore.services;

import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.models.entities.Author;
import com.school.bookstore.repositories.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorDTO getAuthorById(Long id) {
        Author author =  authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFullName(author.getFullName());
        authorDTO.setAuthorInfo(author.getAuthorInformation());

        return authorDTO;
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> AuthorDTO.builder()
                        .id(author.getId())
                        .fullName(author.getFullName())
                        .authorInfo(author.getAuthorInformation())
                        .build())
                .toList();
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author =  authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        author.setAuthorInformation(authorDTO.getAuthorInfo());
        author = authorRepository.save(author);

        return AuthorDTO.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .authorInfo(author.getAuthorInformation())
                .build();
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
