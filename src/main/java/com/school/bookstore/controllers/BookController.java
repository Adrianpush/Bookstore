package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.services.BookService;
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
    public ResponseEntity<BookDTO> createBook(@Validated @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping
    public ResponseEntity<BookDTO> updateBook(@Validated @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(bookDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        return ResponseEntity.ok("Book with id ".concat(bookService.deleteBook(id).toString().concat(" was deleted.")));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getFilteredBooks(@RequestParam String title, @RequestParam String author, @RequestParam int yearPublished) {
        return ResponseEntity.ok(bookService.getFilteredBooks(title, author, yearPublished));
    }

    @GetMapping("/user")
    public ResponseEntity<List<BookDTO>> getBooksFavoritedByUser(@RequestParam Long userID) {
        return ResponseEntity.ok(bookService.getBooksFavoritedByUser(userID));
    }
}
