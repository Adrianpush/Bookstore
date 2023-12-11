package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.enums.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomBookRepository {
    List<Book> findBooksByTitleOrAuthorNameAndGenreAndLanguage(
            String searchString,
            String genre,
            Language language);
}