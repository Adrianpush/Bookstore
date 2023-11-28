package com.school.bookstore.services;

import com.school.bookstore.exceptions.BookCreateException;
import com.school.bookstore.exceptions.BookNotFoundException;
import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.dtos.GenreTagDTO;
import com.school.bookstore.models.entities.Author;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.repositories.AuthorRepository;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.repositories.GenreTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreTagRepository genreTagRepository;
    private final AuthorService authorService;
    private final GenreTagService genreTagService;
    private final ImageUploadService imageUploadService;
    private final String defaultImageLink;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreTagRepository genreTagRepository, AuthorService authorService, GenreTagService genreTagService, ImageUploadService imageUploadService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreTagRepository = genreTagRepository;
        this.authorService = authorService;
        this.genreTagService = genreTagService;
        this.imageUploadService = imageUploadService;
        this.defaultImageLink = "https://dkckcusqogzbwetnizwe.supabase.co/storage/v1/object/public/books/default-book-cover.jpg";
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        if(isDuplicate(bookDTO)) {
            throw new BookCreateException("Book already in database.");
        }
        Book book = convertToBookEntity(bookDTO);
        book.setImageLink(defaultImageLink);
        Book bookEntity = bookRepository.save(book);
        return convertToBookDTO(bookEntity);
    }

    @Override
    public BookDTO getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not in database."));
        return convertToBookDTO(book);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(this::convertToBookDTO).toList();
    }

    @Override
    public List<BookDTO> getFilteredBooks(String title, String authorName, String genre, String language, String publisher) {
        return bookRepository.findAll().stream().map(this::convertToBookDTO).toList();
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO) {
        if(!isDuplicate(bookDTO)) {
            throw new BookCreateException("Book already in database.");
        }

        if (bookRepository.existsById(bookId)) {
            Book bookEntity = bookRepository.save(convertToBookEntity(bookDTO));
            return convertToBookDTO(bookEntity);
        }

        throw new BookNotFoundException("Book with id " + bookId + " not in database.");
    }

    @Override
    public void deleteBookById(Long bookId) {
        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
        } else {
            throw new BookNotFoundException("Book with id " + bookId + " not in database.");
        }
    }

    @Override
    public BookDTO changeBookCoverImage(Long bookId, MultipartFile file) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not in database."));

        String imageLink = "";
        if (book.getImageLink().equals(defaultImageLink)) {
            imageLink = imageUploadService.uploadImage(file, bookId.toString());
        } else {
            imageLink = imageUploadService.updateImage(file, bookId.toString());
        }
        book.setImageLink(imageLink);
        Book updatedBook  = bookRepository.save(book);

        return convertToBookDTO(updatedBook);
    }

    private Book convertToBookEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());

        List<Author> authors = new ArrayList<>();
        bookDTO.getAuthorNameList().forEach(
                authorName -> {
                    Optional<Author> author = authorRepository.findByFullName(authorName);
                    if (author.isPresent()) {
                        authors.add(author.get());
                    } else {
                        AuthorDTO authorDTO = authorService.createAuthor(authorName);
                        Author newAuthor = authorRepository.findById(authorDTO.getId()).get();
                        authors.add(newAuthor);
                    }
                }
        );
        book.setAuthors(authors);
        book.setPublisher(bookDTO.getPublisher());

        Set<GenreTag> genreTagSet = new HashSet<>();
        bookDTO.getGenreTagList().forEach(
                genreName -> {
                    Optional<GenreTag> genreTag = genreTagRepository.findByGenre(genreName);
                    if(genreTag.isPresent()) {
                        genreTagSet.add(genreTag.get());
                    } else {
                        GenreTagDTO genreTagDTO = genreTagService.createGenreTag(genreName);
                        genreTagSet.add(genreTagRepository.findById(genreTagDTO.getId()).get());
                    }
                }
        );
        book.setGenreTagSet(genreTagSet);

        book.setYearPublished(bookDTO.getYearPublished());
        book.setDescription(bookDTO.getDescription());
        book.setLanguage(bookDTO.getLanguage());
        book.setNumPages(bookDTO.getNumPages());
        book.setPriceBeforeDiscount(bookDTO.getPriceBeforeDiscount());
        book.setDiscountPercent(bookDTO.getDiscountPercent());
        book.setCopiesAvailable(bookDTO.getCopiesAvailable());

        return book;
    }

    private BookDTO convertToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthorNameList(book.getAuthors().stream().map(Author::getFullName).toList());
        bookDTO.setGenreTagList(book.getGenreTagSet().stream().map(GenreTag::getGenre).toList());
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setYearPublished(book.getYearPublished());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setLanguage(book.getLanguage());
        bookDTO.setNumPages(book.getNumPages());
        bookDTO.setPriceBeforeDiscount(book.getPriceBeforeDiscount());
        bookDTO.setDiscountPercent(book.getDiscountPercent());
        bookDTO.setCopiesAvailable(book.getCopiesAvailable());
        bookDTO.setImageLink(book.getImageLink());

        return bookDTO;
    }

    private boolean isDuplicate(BookDTO bookDTO) {
        return !bookRepository.findByTitleAndPublisher(bookDTO.getTitle(), bookDTO.getPublisher()).isEmpty();
    }
}
