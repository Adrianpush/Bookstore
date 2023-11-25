package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Validated
@RequestMapping("/api/books")
public class BookController {

    BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @GetMapping("filtered/")
    public ResponseEntity<List<BookDTO>> getFilteredBooks(@RequestParam String title, @RequestParam String authorName,
                                                          @RequestParam String genre, @RequestParam String language,
                                                          @RequestParam String publisher) {
        return ResponseEntity.ok(bookService.getFilteredBooks(title, authorName, genre, language, publisher));
    }

    @GetMapping()
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
}
