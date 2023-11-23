package com.school.bookstore.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BookDTO {

    Long id;
    @NotEmpty
    String title;
    @NotEmpty
    String author;
    @NotEmpty
    int yearPublished;
    String description;
    String genre;
    int numPages;
}
