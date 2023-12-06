package com.school.bookstore.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StaffMemberDTO {

    private Long id;
    @NotBlank(message = "Name must not be blank.")
    private String fullName;
    @NotBlank(message = "Username must not be blank.")
    private String username;
    @NotBlank(message = "Must set a password.")
    private String password;
}
