package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.GenreTagDTO;
import com.school.bookstore.models.entities.GenreTag;

import java.util.List;
import java.util.Optional;

public interface GenreTagService {

    GenreTag createGenreTag(String genreName);

    Optional<GenreTag> getGenreTag(String genreName);

    List<GenreTagDTO> getAllGenreTags();
}