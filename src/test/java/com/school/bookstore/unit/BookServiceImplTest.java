package com.school.bookstore.unit;

import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.entities.Author;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.GenreTag;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.services.implementations.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static com.school.bookstore.services.implementations.BookServiceImpl.DEFAULT_IMAGE_LINK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    void createBookShouldPass() {
        //Given
        BookDTO bookDTO = createValidBookDTO();
        Book book = createValidBook();


        //When
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        //Act
        BookDTO result = bookServiceImpl.createBook(bookDTO);

        //Then
        assertNotNull(bookDTO);
        assertEquals(DEFAULT_IMAGE_LINK, result.getImageLink());
    }

    private BookDTO createValidBookDTO() {
        return BookDTO.builder()
                .title("Book")
                .authorNameList(List.of("Author"))
                .genreTagList(List.of("Fantasy"))
                .publisher("Publisher")
                .build();
    }

    private Book createValidBook() {
        Author author = new Author();
        author.setId(1L);
        author.setFullName("author");
        GenreTag genreTag = new GenreTag();
        genreTag.setId(1L);
        genreTag.setGenre("Software Testing");

        return Book.builder()
                .id(1L)
                .title("Book")
                .authors(Set.of(author))
                .genreTagSet(Set.of(genreTag))
                .publisher("Publisher")
                .imageLink(DEFAULT_IMAGE_LINK)
                .build();
    }
}
