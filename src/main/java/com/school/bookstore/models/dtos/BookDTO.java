package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(min = 2, message = "{validation.title.size.too_short}")
    @Size(max = 200, message = "{validation.title.size.too_long}")
    private String title;

    @NotEmpty
    private List<String> authorNameList;

    @NotEmpty
    private List<String> genreTagList;

    @NotBlank
    @Size(min = 2, message = "{validation.publisher.size.too_short}")
    @Size(max = 200, message = "{validation.publisher.size.too_long}")
    private String publisher;

    private int yearPublished;
    @Size(min = 5, message = "{validation.description.size.too_short}")
    @Size(max = 5000, message = "{validation.description.size.too_long}")

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
