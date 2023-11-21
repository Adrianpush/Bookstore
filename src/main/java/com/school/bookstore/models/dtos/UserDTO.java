package com.school.bookstore.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {

    Long id;
    @NotEmpty
    String firstName;
    @NotEmpty
    String lastName;
    String eMail;
}
