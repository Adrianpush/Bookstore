package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Author;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.GenreTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository {
    Optional<Book> findByTitleAndPublisher(String title, String publisher);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Book b JOIN b.authors a WHERE a = :author")
    boolean existsByAuthor(@Param("author") Author author);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Book b JOIN b.genreTagSet a WHERE a = :genreTag")
    boolean existsByGenreTag(@Param("genreTag") GenreTag genreTag);
}