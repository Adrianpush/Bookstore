package com.school.bookstore.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
public class AuthorDTO {

    Long id;
    String fullName;
    String AuthorInformation;
}
