package com.school.bookstore.services;

import com.school.bookstore.models.dtos.BookDTO;
import java.util.Set;

public interface RecommendationService {

    Set<BookDTO> getRecommendations(String customerEmail);
}
