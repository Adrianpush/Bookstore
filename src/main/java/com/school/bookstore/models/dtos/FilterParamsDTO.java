package com.school.bookstore.models.dtos;

import lombok.Data;

@Data
public class FilterParamsDTO {

    String title;
    String author;
    String genre;
    String language;
    String publisher;
}
