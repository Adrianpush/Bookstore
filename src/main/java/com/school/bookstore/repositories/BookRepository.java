package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.models.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleAndPublisher(String title, String publisher);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE REPLACE(LOWER(b.title), ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :title, '%')), ' ', '') " +
            "OR REPLACE(LOWER(a.fullName), ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :authorName, '%')), ' ', '')")
    List<Book> findBooksByTitleAndAuthorName(@Param("title") String title, @Param("authorName") String authorName);

    @Query("SELECT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN b.genreTagSet t " +
            "WHERE REPLACE(LOWER(b.title), ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :title, '%')), ' ', '') " +
            "   OR REPLACE(LOWER(a.fullName), ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :authorName, '%')), ' ', '') " +
            "   AND t.genre = :genre " +
            "   AND b.language = :language")
    List<Book> findBooksByTitleOrAuthorNameAndGenreAndLanguage(
            @Param("title") String title,
            @Param("authorName") String authorName,
            @Param("genre") String genre,
            @Param("language") Language language);

}