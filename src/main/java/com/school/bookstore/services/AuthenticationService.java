package com.school.bookstore.services;

import com.school.bookstore.models.dtos.JwtAuthenticationResponseDTO;
import com.school.bookstore.models.dtos.SignInRequestDTO;
import com.school.bookstore.models.entities.User;
import com.school.bookstore.repositories.UserRepository;
import com.school.bookstore.services.interfaces.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponseDTO signIn(SignInRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponseDTO.builder()
                .token(jwt)
                .build();
    }
}
