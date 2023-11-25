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

    @Query("SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN b.authors a " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:genreTag IS NULL OR :genreTag MEMBER OF b.genreTagSet) " +
            "AND (:authorName IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :authorName, '%'))) " +
            "AND (:publisher IS NULL OR LOWER(b.publisher) LIKE LOWER(CONCAT('%', :publisher, '%'))) " +
            "AND (:language IS NULL OR b.language = :language)")
    List<Book> findBooksByAttributes(
            @Param("title") String title,
            @Param("genreTag") GenreTag genreTag,
            @Param("authorName") String authorName,
            @Param("publisher") String publisher,
            @Param("language") Language language
    );

}