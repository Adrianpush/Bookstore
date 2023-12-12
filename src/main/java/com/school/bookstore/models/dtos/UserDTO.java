package com.school.bookstore.models.dtos;

import com.school.bookstore.models.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Email must not be blank.")
    @Email
    private String email;

    @NotBlank(message = "Must set a password.")
    private String password;

    @NotBlank(message = "Customer name must not be blank.")
    private String fullName;

    private String address;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Role role;
}