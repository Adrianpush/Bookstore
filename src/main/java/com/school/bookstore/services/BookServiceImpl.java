package com.school.bookstore.services;

import com.school.bookstore.exceptions.BookCreateException;
import com.school.bookstore.exceptions.BookNotFoundException;
import com.school.bookstore.exceptions.UserNotFoundException;
import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.User;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookRepository.findByTitleAndByAuthor(bookDTO.getTitle(), bookDTO.getAuthor());
        if (book != null) {
            throw new BookCreateException("Book already exists");
        }
        Book bookEntity = bookRepository.save(convertToBook(bookDTO));
        return convertToBookDTO(bookEntity);
    }

    @Override
    public BookDTO getBookById(Long id) {
        return convertToBookDTO(
                bookRepository.findById(id)
                        .orElseThrow(() -> new BookNotFoundException("Cannot find book with id " + id)));
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO) {

        Book previousBookEntity = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new BookNotFoundException("Cannot find book with id " + bookDTO.getId()));
        previousBookEntity.setTitle(bookDTO.getTitle());
        previousBookEntity.setAuthor(bookDTO.getAuthor());
        previousBookEntity.setDescription(bookDTO.getDescription());
        previousBookEntity.setYearPublished(bookDTO.getYearPublished());
        previousBookEntity.setNumPages(bookDTO.getNumPages());
        previousBookEntity.setGenre(bookDTO.getGenre());
        Book updatedBookEntity = bookRepository.save(previousBookEntity);
        return convertToBookDTO(updatedBookEntity);
    }

    @Override
    public Long deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        return id;
    }

    @Override
    public List<BookDTO> getFilteredBooks(String title, String author, int yearPublished) {
        return bookRepository.findAllByTitleAndAuthorAndYearPublished(title, author, yearPublished)
                .stream().map(this::convertToBookDTO).toList();
    }

    @Override
    public List<BookDTO> getBooksFavoritedByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return bookRepository.findFavoritedBooksByUserId(user.getId())
                .stream().map(this::convertToBookDTO)
                .toList();
    }

    private BookDTO convertToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setYearPublished(book.getYearPublished());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setNumPages(book.getNumPages());
        return bookDTO;
    }

    private Book convertToBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setDescription(bookDTO.getDescription());
        book.setYearPublished(bookDTO.getYearPublished());
        book.setGenre(bookDTO.getGenre());
        book.setNumPages(bookDTO.getNumPages());
        return book;
    }
}
