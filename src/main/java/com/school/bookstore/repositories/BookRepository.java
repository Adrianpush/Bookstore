package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT book FROM Book book "
            + "WHERE book.title = :title "
            + "AND book.author = :author ")
    Book findByTitleAndByAuthor(@Param("title") String title, @Param("author") String author);

    @Query("SELECT book FROM Book book " +
            "WHERE book.title = :title " +
            "AND book.author = :author " +
            "AND book.yearPublished = :yearPublished")
    List<Book> findAllByTitleAndAuthorAndYearPublished(
            @Param("title") String title,
            @Param("author") String author,
            @Param("yearPublished") int yearPublished);

    @Query("SELECT b FROM User u JOIN u.favoriteBooks b WHERE u.id = :userId")
    List<Book> findFavoritedBooksByUserId(@Param("userId") Long userId);
}
