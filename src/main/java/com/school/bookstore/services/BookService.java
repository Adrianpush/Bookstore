package com.school.bookstore.services;

import com.school.bookstore.models.dtos.BookDTO;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);
    BookDTO getBookById(Long id);
    BookDTO updateBook(BookDTO bookDTO);
    Long deleteBook(Long id);
}
