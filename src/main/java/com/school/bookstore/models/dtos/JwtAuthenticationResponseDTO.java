package com.school.bookstore.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthenticationResponseDTO {

    String token;
}