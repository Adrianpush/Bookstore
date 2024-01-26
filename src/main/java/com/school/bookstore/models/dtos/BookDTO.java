package com.school.bookstore.models.dtos;

import com.school.bookstore.models.enums.Language;
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

    @Size(min = 2, message = "{validation.title.size.too_short}")
    @Size(max = 200, message = "{validation.title.size.too_long}")
    @NotBlank(message = "Field must not be blank")
    private String title;

    @NotEmpty(message = "Author list must not be blank")
    private List<AuthorDTO> authorDTOS;

    @NotEmpty(message = "Tag list must not be blank")
    private List<String> genreTagList;

    @NotBlank(message = "Publisher must not be blank")
    @Size(min = 2, message = "{validation.publisher.size.too_short}")
    @Size(max = 200, message = "{validation.publisher.size.too_long}")
    private String publisher;

    private int yearPublished;

    private String description;

    private Language language;

    @Min(0)
    private int numPages;

    @Min(0)
    private double priceBeforeDiscount;

    @Min(0)
    @Max(99)
    private int discountPercent;

    @Min(0)
    private int copiesAvailable;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String imageLink;
}
