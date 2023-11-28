package com.school.bookstore.services;

import com.school.bookstore.models.dtos.BookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long bookId);

    List<BookDTO> getAllBooks();

    List<BookDTO> getFilteredBooks(String title, String authorName, String genre, String language, String publisher);

    BookDTO updateBook(Long bookId, BookDTO bookDTO);

    BookDTO changeBookCoverImage(Long bookId, MultipartFile file);



    void deleteBookById(Long bookId);
}
