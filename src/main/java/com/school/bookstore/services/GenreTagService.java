package com.school.bookstore.services;

import com.school.bookstore.models.dtos.GenreTagDTO;

public interface GenreTagService {

    GenreTagDTO createGenreTag(String genreName);
}
