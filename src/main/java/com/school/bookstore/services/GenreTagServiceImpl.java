package com.school.bookstore.services;

import com.school.bookstore.models.dtos.GenreTagDTO;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.repositories.GenreTagRepository;
import com.school.bookstore.services.interfaces.GenreTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreTagServiceImpl implements GenreTagService {

    private final GenreTagRepository genreTagRepository;

    @Override
    public List<GenreTagDTO> getAllGenreTags() {
        return genreTagRepository.findAll()
                .stream()
                .map(genreTag -> GenreTagDTO.builder()
                        .genre(genreTag.getGenre())
                        .id(genreTag.getId())
                        .build())
                .toList();
    }

    @Override
    public GenreTag createGenreTag(String genreName) {
        GenreTag genreTag = new GenreTag();
        genreTag.setGenre(genreName);

        return genreTag;
    }

    @Override
    public Optional<GenreTag> getGenreTag(String genreName) {
        return genreTagRepository.findByGenre(genreName);
    }
}
