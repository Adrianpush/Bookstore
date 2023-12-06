package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository {
    Optional<Book> findByTitleAndPublisher(String title, String publisher);
}
