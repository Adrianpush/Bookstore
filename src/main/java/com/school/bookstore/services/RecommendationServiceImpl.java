package com.school.bookstore.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.bookstore.exceptions.book.RecommendationException;
import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.models.dtos.ChatRequestDTO;
import com.school.bookstore.models.dtos.ChatResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {


    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final BookService bookService;


    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Override
    public Set<BookDTO> getRecommendations(String email) {

        String prompt = ("Tell me just the title and author, no quotes and separated by \"/\", " +
                "of 3 books, separated by  \"/\", recommended for some who previously bought ")
                .concat(orderService.getBooksBought(email).toString());

        ChatRequestDTO chatRequestDTO = new ChatRequestDTO(model, prompt);
        try {
            String jsonBody = objectMapper.writeValueAsString(chatRequestDTO);
            RequestBody requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .method("POST", requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + openaiApiKey)
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            String gptResponse = getGPTResponse(client, request).getChoices().get(0).getMessage().getContent();
            return findRecommendedBooks(gptResponse);

        } catch (IOException ioException) {
            log.info(ioException.getMessage());
            throw new RecommendationException("IO Exception");
        }
    }

    @NotNull
    private ChatResponseDTO getGPTResponse(OkHttpClient client, Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, ChatResponseDTO.class);
            }
            throw new RecommendationException("Response not valid");
        } catch (IOException e) {
            throw new RecommendationException("IO Exception");
        }
    }

    private Set<BookDTO> findRecommendedBooks(String bookRecommendations) {
        String[] bookTitlesAndAuthors = bookRecommendations.split("/");
        Set<BookDTO> recommendedBooks = new HashSet<>();
        for (String searchString : bookTitlesAndAuthors) {
            log.info(searchString);
            recommendedBooks.addAll(bookService.getBooks(searchString.trim(), null, null));
        }
        return recommendedBooks;
    }
}



