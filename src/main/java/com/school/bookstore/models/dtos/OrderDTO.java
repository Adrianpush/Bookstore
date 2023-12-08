package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long customerId;
    @NotEmpty(message = "Cannot create order from empty shopping cart.")
    List<OrderItemDTO> orderItems;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    OrderStatus orderStatus;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime createdAt;
}
