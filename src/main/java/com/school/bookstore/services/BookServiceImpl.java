package com.school.bookstore.services;

import com.school.bookstore.exceptions.BookCreateException;
import com.school.bookstore.models.dtos.AuthorDTO;
import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.dtos.FilterParamsDTO;
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

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreTagRepository genreTagRepository, AuthorService authorService, GenreTagService genreTagService, ImageUploadService imageUploadService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreTagRepository = genreTagRepository;
        this.authorService = authorService;
        this.genreTagService = genreTagService;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO, MultipartFile multipartFile) {
        if(isDuplicate(bookDTO)) {
            throw new BookCreateException("Book already in database.");
        }
        if (multipartFile != null) {
            String imageLink = imageUploadService.uploadImage(multipartFile, String.valueOf(bookDTO.getTitle().concat(bookDTO.getPublisher()).hashCode()));
            bookDTO.setImageLink(imageLink);
        }
        Book book = bookRepository.save(convertToBookEntity(bookDTO));
        return convertToBookDTO(book);
    }

    @Override
    public List<BookDTO> getFilteredBooks(String title, String authorName, String genre, String language, String publisher) {

        return null;
    }

    @Override
    public List<BookDTO> getFilteredBooks(FilterParamsDTO filterParamsDTO) {
        GenreTag genreTag = genreTagRepository.findByGenre(filterParamsDTO.getGenre()).orElse(null);
        return null;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(this::convertToBookDTO).toList();
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
        book.setImageLink(bookDTO.getImageLink());

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
