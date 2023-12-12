package com.school.bookstore.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderSummaryDTO {

    String recipientName;
    String recipientEmail;
    List<String> books;
}
