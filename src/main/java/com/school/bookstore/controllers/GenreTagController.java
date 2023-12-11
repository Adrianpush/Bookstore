package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.GenreTagDTO;
import com.school.bookstore.services.interfaces.GenreTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{genreId}")
    public ResponseEntity<GenreTagDTO> getTagById(@PathVariable Long genreId) {
        return ResponseEntity.ok(genreTagService.getTagById(genreId));
    }

    @PutMapping("/{genreId}")
    public ResponseEntity<GenreTagDTO> updateGenreTag(
            @PathVariable Long genreId,
            @RequestBody @Valid GenreTagDTO genreTagDTO) {
        return ResponseEntity.ok(genreTagService.updateGenreTag(genreId, genreTagDTO));
    }
}
