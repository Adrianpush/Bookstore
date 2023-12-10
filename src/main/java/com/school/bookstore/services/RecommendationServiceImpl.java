package com.school.bookstore.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.bookstore.models.dtos.ChatRequestDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtService jwtService;

    @Autowired
    OrderService orderService;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.org}")
    private String openOrg;

    @Override
    public String getRecommendations(String email) {

        String prompt = "Please make 5 book recommendations for a person who bought "
                .concat(orderService.getBooksBought(email).toString());

        ChatRequestDTO chatRequestDTO = new ChatRequestDTO(model, prompt);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = null;
        try {
            body = RequestBody.create(objectMapper.writeValueAsString(chatRequestDTO), mediaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        Request request = new Request.Builder()
                .url(apiUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", openaiApiKey)
                .addHeader("OpenAI-Organization", openOrg)
                .build();


        try(Response response = client.newCall(request).execute()) {
            return response.toString();
        } catch (IOException e) {
            return null;
        }
    }

}
