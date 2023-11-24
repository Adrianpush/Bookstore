package com.school.bookstore.services;

import com.school.bookstore.models.dtos.GenreTagDTO;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.repositories.GenreTagRepository;
import org.springframework.stereotype.Service;

@Service
public class GenreTagServiceImpl implements GenreTagService {

    GenreTagRepository genreTagRepository;

    public GenreTagServiceImpl(GenreTagRepository genreTagRepository) {
        this.genreTagRepository = genreTagRepository;
    }

    @Override
    public GenreTagDTO createGenreTag(String genreName) {
        GenreTag genreTag = new GenreTag();
        genreTag.setGenre(genreName);
        genreTag = genreTagRepository.save(genreTag);
        GenreTagDTO genreTagResponseDTO = new GenreTagDTO();
        genreTagResponseDTO.setId(genreTag.getId());
        genreTagResponseDTO.setGenre(genreTag.getGenre());

        return genreTagResponseDTO;
    }
}
