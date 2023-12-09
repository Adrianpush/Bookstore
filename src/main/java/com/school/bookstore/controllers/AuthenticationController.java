package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.JwtAuthenticationResponseDTO;
import com.school.bookstore.models.dtos.SignInRequestDTO;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<JwtAuthenticationResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authenticationService.signup(userDTO));
    }

    @GetMapping
    ResponseEntity<JwtAuthenticationResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequestDTO));
    }
}
