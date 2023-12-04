package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id;
    Long customerId;
    List<OrderItemDTO> orderItems;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    OrderStatus orderStatus;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime createdAt;
}
