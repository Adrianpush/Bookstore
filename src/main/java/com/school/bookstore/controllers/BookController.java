package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.services.BookService;
import com.school.bookstore.services.ImageUploadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Validated
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService, ImageUploadService imageUploadService) {
        this.bookService = bookService;
    }
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestPart("bookDTO") @Valid BookDTO bookDTO, @RequestPart("imageJpg") MultipartFile multipartFile) {
        return ResponseEntity.ok(bookService.createBook(bookDTO, multipartFile));
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
