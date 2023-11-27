package com.school.bookstore.services;

import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.dtos.FilterParamsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO, MultipartFile multipartFile);
    List<BookDTO> getFilteredBooks(String title, String authorName, String genre, String language, String publisher);
    List<BookDTO> getFilteredBooks(FilterParamsDTO filterParamsDTO);
    List<BookDTO> getAllBooks();
}
