package com.school.bookstore.services;

import com.school.bookstore.exceptions.book.BookCreateException;
import com.school.bookstore.exceptions.book.BookNotFoundException;
import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.entities.Author;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.models.enums.Language;
import com.school.bookstore.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String DEFAULT_IMAGE_LINK = "https://dkckcusqogzbwetnizwe.supabase.co" +
            "/storage/v1/object/public/books/default-book-cover.jpg";
    private static final String BOOK_NOT_FOUND_MESSAGE = "Database doesn't contain any book with id %s.";
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreTagService genreTagService;
    private final ImageUploadService imageUploadService;

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        checkForDuplicate(bookDTO);
        Book book = convertToBookEntity(bookDTO);
        book.setImageLink(DEFAULT_IMAGE_LINK);
        Book bookEntity = bookRepository.save(book);
        return convertToBookDTO(bookEntity);
    }

    @Override
    public BookDTO getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));
        return convertToBookDTO(book);
    }

    @Override
    public List<BookDTO> getBooks(String searchString, String genre, String language) {
        Language lang;
        try {
            lang = Language.valueOf(language.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException exception) {
            lang = null;
        }

        return bookRepository.findBooksByTitleOrAuthorNameAndGenreAndLanguage(searchString, genre, lang)
                .stream()
                .map(this::convertToBookDTO)
                .toList();
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO) {
        Book bookToBeModified = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));

        if (!(bookToBeModified.getPublisher().equals(bookDTO.getPublisher()) &&
                bookToBeModified.getTitle().equals(bookDTO.getTitle()))) {
            checkForDuplicate(bookDTO);
        }
        Book updatedBook = convertToBookEntity(bookDTO);
        updatedBook.setId(bookToBeModified.getId());
        updatedBook.setImageLink(bookToBeModified.getImageLink());
        updatedBook = bookRepository.save(updatedBook);
        return convertToBookDTO(updatedBook);
    }

    @Override
    public void deleteBookById(Long bookId) {
        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
        } else {
            throw new BookNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId));
        }
    }

    @Override
    public BookDTO changeBookCoverImage(Long bookId, MultipartFile file) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MESSAGE.formatted(bookId)));

        String imageLink;
        if (book.getImageLink().equals(DEFAULT_IMAGE_LINK)) {
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
                .description(bookDTO.getDescription())
                .genreTagSet(genreTagSet)
                .yearPublished(bookDTO.getYearPublished())
                .language(bookDTO.getLanguage())
                .numPages(bookDTO.getNumPages())
                .priceBeforeDiscount(bookDTO.getPriceBeforeDiscount())
                .discountPercent(bookDTO.getDiscountPercent())
                .copiesAvailable(bookDTO.getCopiesAvailable())
                .build();
    }

    private BookDTO convertToBookDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorNameList(book.getAuthors().stream().map(Author::getFullName).toList())
                .genreTagList(book.getGenreTagSet().stream().map(GenreTag::getGenre).toList())
                .publisher(book.getPublisher())
                .yearPublished(book.getYearPublished())
                .description(book.getDescription())
                .language(book.getLanguage())
                .numPages(book.getNumPages())
                .priceBeforeDiscount(book.getPriceBeforeDiscount())
                .discountPercent(book.getDiscountPercent())
                .copiesAvailable(book.getCopiesAvailable())
                .imageLink(book.getImageLink())
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

    private void checkForDuplicate(BookDTO bookDTO) {
        Optional<Book> bookOptional = bookRepository.findByTitleAndPublisher(bookDTO.getTitle(), bookDTO.getPublisher());
        if (bookOptional.isPresent()) {
            throw new BookCreateException("Book already in database with id: " + bookOptional.get().getId());
        }
    }
}
