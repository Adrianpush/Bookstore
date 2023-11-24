package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.Author;
import com.school.bookstore.models.entities.Language;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {

    Long id;
    @NotBlank
    String title;
    List<String> authorNameList;
    List<String> genreTagList;
    @NotBlank
    String publisher;
    int yearPublished;
    String description;
    Language language;
    int numPages;
    @Min(0)
    double priceBeforeDiscount;
    @Min(0) @Max(99)
    int discountPercent;
    @Min(0)
    int copiesAvailable;
}
