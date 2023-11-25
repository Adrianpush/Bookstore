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

    private Long id;
    @NotBlank
    private String title;
    private List<String> authorNameList;
    private List<String> genreTagList;
    @NotBlank
    private String publisher;
    private int yearPublished;
    private String description;
    private Language language;
    private int numPages;
    @Min(0)
    private double priceBeforeDiscount;
    @Min(0) @Max(99)
    private int discountPercent;
    @Min(0)
    private int copiesAvailable;
}
