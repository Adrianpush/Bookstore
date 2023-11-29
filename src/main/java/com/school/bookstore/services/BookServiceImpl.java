package com.school.bookstore.services;

import com.school.bookstore.exceptions.BookCreateException;
import com.school.bookstore.exceptions.BookNotFoundException;
import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.entities.Author;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.repositories.BookRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreTagService genreTagService;
    private final ImageUploadService imageUploadService;
    private final String defaultImageLink;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, GenreTagService genreTagService, ImageUploadService imageUploadService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreTagService = genreTagService;
        this.imageUploadService = imageUploadService;
        this.defaultImageLink = "${image.urlBase}".concat("default-book-cover.jpg");

    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        if (isDuplicate(bookDTO)) {
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
        if (!isDuplicate(bookDTO)) {
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
        Book updatedBook = bookRepository.save(book);

        return convertToBookDTO(updatedBook);
    }

    private Book convertToBookEntity(BookDTO bookDTO) {

        Set<Author> authors = getAuthors(bookDTO.getAuthorNameList());
        Set<GenreTag> genreTagSet = getGenreTags(bookDTO.getGenreTagList());

        return Book.builder()
                .title(bookDTO.getTitle())
                .authors(authors)
                .publisher(bookDTO.getPublisher())
                .genreTagSet(genreTagSet)
                .yearPublished(bookDTO.getYearPublished())
                .language(bookDTO.getLanguage())
                .numPages(bookDTO.getNumPages())
                .discountPercent(bookDTO.getDiscountPercent())
                .copiesAvailable(bookDTO.getCopiesAvailable())
                .build();
    }

    @NotNull
    private Set<Author> getAuthors(List<String> authorNames) {
        Set<Author> authors = new HashSet<>();
        if (authorNames != null) {
            authorNames.forEach(
                    authorName -> {
                        Optional<Author> author = authorService.getAuthorByName(authorName);
                        if (author.isPresent()) {
                            authors.add(author.get());
                        } else {
                            authors.add(authorService.createAuthor(authorName));
                        }
                    }
            );
        }

        return authors;
    }

    @NotNull
    private Set<GenreTag> getGenreTags(List<String> genreTags) {
        Set<GenreTag> genreTagSet = new HashSet<>();
        if (genreTags != null) {
            genreTags.forEach(
                    genreName -> {
                        Optional<GenreTag> genreTag = genreTagService.getGenreTag(genreName);
                        if (genreTag.isPresent()) {
                            genreTagSet.add(genreTag.get());
                        } else {
                            genreTagSet.add(genreTagService.createGenreTag(genreName));
                        }
                    }
            );
        }

        return genreTagSet;
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
