package com.school.bookstore.models.dtos;

import lombok.Data;

@Data
public class FilterParamsDTO {

    private String title;
    private String author;
    private String genre;
    private String language;
    private String publisher;
}
