package com.school.bookstore.services.implementations;

import com.school.bookstore.exceptions.users.AuthorNotFoundException;
import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.models.entities.Author;
import com.school.bookstore.repositories.AuthorRepository;
import com.school.bookstore.services.interfaces.AuthorService;
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
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
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
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
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
        return author;
    }

    @Override
    public Optional<Author> getAuthorByName(String authorFullName) {
        return authorRepository.findByFullName(authorFullName);
    }

    @Override
    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }
}
