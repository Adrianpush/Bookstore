package com.school.bookstore.services.implementations;

import com.school.bookstore.exceptions.book.GenreTagNotFoundException;
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
    public GenreTagDTO getTagById(Long genreTagId) {
        GenreTag genreTag = genreTagRepository.findById(genreTagId)
                .orElseThrow(() -> new GenreTagNotFoundException("No genre tag with id " + genreTagId));
        return new GenreTagDTO(genreTag.getId(), genreTag.getGenre());
    }

    @Override
    public GenreTagDTO updateGenreTag(Long genreTagId, GenreTagDTO genreTagDTO) {
        if (genreTagRepository.existsById(genreTagId)) {
            GenreTag updatedGenreTag = new GenreTag();
            updatedGenreTag.setId(genreTagId);
            updatedGenreTag.setGenre(genreTagDTO.getGenre());
            updatedGenreTag = genreTagRepository.save(updatedGenreTag);
            return new GenreTagDTO(updatedGenreTag.getId(), updatedGenreTag.getGenre());
        }
        throw new GenreTagNotFoundException("No genre tag with id " + genreTagId);
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

    @Override
    public void deleteGenreTag(GenreTag genreTag) {
        genreTagRepository.delete(genreTag);
    }
}
