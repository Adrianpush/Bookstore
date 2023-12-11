package com.school.bookstore.models.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailDTO {

    private String from;
    private String to;
    private String subject;
    private String body;
}