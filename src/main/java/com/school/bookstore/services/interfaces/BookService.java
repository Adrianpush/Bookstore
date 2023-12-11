package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.BookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long bookId);

    BookDTO updateBook(Long bookId, BookDTO bookDTO);

    BookDTO changeBookCoverImage(Long bookId, MultipartFile file);

    void deleteBookById(Long bookId);

    List<BookDTO> getBooks(String searchString, String genre, String language);
}