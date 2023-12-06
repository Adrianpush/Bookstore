package com.school.bookstore.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id;
    @NotNull
    Long bookId;
    @NotNull
    Integer quantity;
}
