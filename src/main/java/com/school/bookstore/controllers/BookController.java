package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.services.interfaces.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "Books API", description = "Endpoints for managing books")
@Validated
@RequestMapping("/api/books")
@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Secured("ROLE_STAFF")
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks(
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            @RequestParam int pageNum) {
        return ResponseEntity.ok(bookService.getBooks(searchString, genre, language, pageNum));
    }

    @Secured("ROLE_STAFF")
    @PutMapping("/{bookId}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long bookId, @RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(bookId, bookDTO));
    }

    @Secured("ROLE_STAFF")
    @DeleteMapping("/{bookId}")
    public HttpStatus deleteBook(@PathVariable Long bookId) {
        bookService.deleteBookById(bookId);
        return HttpStatus.NO_CONTENT;
    }

    @Secured("ROLE_STAFF")
    @PatchMapping("/{bookId}/cover")
    public ResponseEntity<BookDTO> addBookCoverImage(@PathVariable Long bookId, @RequestBody MultipartFile file) {
        return ResponseEntity.ok(bookService.changeBookCoverImage(bookId, file));
    }
}
