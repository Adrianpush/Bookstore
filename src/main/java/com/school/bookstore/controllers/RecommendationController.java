package com.school.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.bookstore.models.dtos.ChatRequestDTO;
import com.school.bookstore.models.dtos.ChatResponseDTO;
import com.school.bookstore.services.JwtService;
import com.school.bookstore.services.OrderService;
import com.school.bookstore.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
@RestController
public class RecommendationController {


    private final JwtService jwtService;
    private final RecommendationService recommendationService;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.org}")
    private String openOrg;

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<String> chat(@RequestHeader(name = "Authorization") String authorizationHeader) throws IOException {

        String requesterEmail = jwtService.extractUserName(authorizationHeader.substring(7));
        return ResponseEntity.ofNullable(recommendationService.getRecommendations(requesterEmail));
    }
}