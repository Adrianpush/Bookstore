package com.school.bookstore.services;

import com.school.bookstore.models.dtos.BookDTO;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);
    BookDTO getBookById(Long id);
    BookDTO updateBook(BookDTO bookDTO);
    Long deleteBook(Long id);
    List<BookDTO> getFilteredBooks(String title, String author, int yearPublished);
    List<BookDTO> getBooksFavoritedByUser(Long userId);
}
