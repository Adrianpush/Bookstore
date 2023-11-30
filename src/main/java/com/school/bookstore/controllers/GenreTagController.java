package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.GenreTagDTO;
import com.school.bookstore.services.GenreTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Genre Tag", description = "Endpoint for getting all genreTags")
@RequiredArgsConstructor
@RequestMapping("/api/genre-tags")
@RestController
public class GenreTagController {

    private final GenreTagService genreTagService;
    @GetMapping
    public ResponseEntity<List<GenreTagDTO>> getAllGenreTags() {
        return ResponseEntity.ok(genreTagService.getAllGenreTags());
    }
}
