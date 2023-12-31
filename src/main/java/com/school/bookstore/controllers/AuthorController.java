package com.school.bookstore.controllers;


import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.services.interfaces.AuthorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Author", description = "Endpoints for getting author, all authors and for adding information")
@RequiredArgsConstructor
@RequestMapping("/api/authors")
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @Secured("ROLE_STAFF")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> addInformation(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
    }
}