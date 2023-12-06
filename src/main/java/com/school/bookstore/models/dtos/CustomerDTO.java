package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotBlank(message = "Customer name must not be blank.")
    private String fullName;
    @NotBlank(message = "Must set a password.")
    private String password;
    @NotBlank(message = "Email must not be blank.")
    @Email
    private String email;
    private String address;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<OrderItem> shoppingCart;

}

