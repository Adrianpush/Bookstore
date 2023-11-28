package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String imageLink;
}
