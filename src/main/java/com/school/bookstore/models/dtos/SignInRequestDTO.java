package com.school.bookstore.models.dtos;

import lombok.Data;

@Data
public class SignInRequestDTO {

    String email;
    String password;
}
