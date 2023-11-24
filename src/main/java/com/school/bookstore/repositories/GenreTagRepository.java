package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.GenreTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreTagRepository extends JpaRepository<GenreTag, Long> {
    Optional<GenreTag> findByGenre(String genreName);
}
