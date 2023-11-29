package com.school.bookstore.services;

import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.repositories.GenreTagRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreTagServiceImpl implements GenreTagService {

    private final GenreTagRepository genreTagRepository;

    public GenreTagServiceImpl(GenreTagRepository genreTagRepository) {
        this.genreTagRepository = genreTagRepository;
    }

    @Override
    public GenreTag createGenreTag(String genreName) {
        GenreTag genreTag = new GenreTag();
        genreTag.setGenre(genreName);
        genreTag = genreTagRepository.save(genreTag);

        return genreTag;
    }

    @Override
    public Optional<GenreTag> getGenreTag(String genreName) {
        return genreTagRepository.findByGenre(genreName);
    }
}
