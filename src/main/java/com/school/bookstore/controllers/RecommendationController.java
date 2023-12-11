package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.BookDTO;
import com.school.bookstore.services.JwtService;
import com.school.bookstore.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
@RestController
public class RecommendationController {

    private final JwtService jwtService;
    private final RecommendationService recommendationService;

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<Set<BookDTO>> chat(@RequestHeader(name = "Authorization") String authorizationHeader) {
        String requesterEmail = jwtService.extractUserName(authorizationHeader.substring(7));
        return ResponseEntity.ok(recommendationService.getRecommendations(requesterEmail));
    }
}