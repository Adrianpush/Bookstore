package com.school.bookstore.services;

import com.school.bookstore.models.entities.GenreTag;

import java.util.Optional;

public interface GenreTagService {

    GenreTag createGenreTag(String genreName);
    Optional<GenreTag> getGenreTag(String genreName);
}
