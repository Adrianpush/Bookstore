package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.JwtAuthenticationResponseDTO;
import com.school.bookstore.models.dtos.SignInRequestDTO;
import com.school.bookstore.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sign In", description = "Endpoint for signing in")
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    ResponseEntity<JwtAuthenticationResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequestDTO));
    }
}
