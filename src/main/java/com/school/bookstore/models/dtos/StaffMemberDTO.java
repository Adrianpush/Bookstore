package com.school.bookstore.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StaffMemberDTO {

    private Long id;
    @NotBlank
    private String fullName;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
