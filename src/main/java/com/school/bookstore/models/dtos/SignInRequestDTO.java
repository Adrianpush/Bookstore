package com.school.bookstore.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignInRequestDTO {

    @NotNull
    String email;
    @NotNull
    String password;
}