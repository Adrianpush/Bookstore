package com.school.bookstore.services;

import com.school.bookstore.exceptions.AuthentificationException;
import com.school.bookstore.models.dtos.JwtAuthenticationResponseDTO;
import com.school.bookstore.models.dtos.SignInRequestDTO;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.models.entities.Role;
import com.school.bookstore.models.entities.User;
import com.school.bookstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponseDTO signup(UserDTO userDTO) {
        userService.checkForDuplicate(userDTO.getEmail());

        User user = User
                .builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .address(userDTO.getAddress())
                .role(Role.ROLE_USER)
                .build();

        user = userService.save(user);
        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponseDTO.builder().token(jwt).build();
    }


    public JwtAuthenticationResponseDTO signin(SignInRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponseDTO.builder().token(jwt).build();
    }

    public boolean isUserAuthorized(Long resourceId, String token) {
        String claimedUserName = jwtService.extractUserName(token);
        String resourceEmail = userRepository.findById(resourceId)
                .orElseThrow(() -> new AuthentificationException("Forbidden"))
                .getEmail();

        return claimedUserName.equals(resourceEmail);
    }
}
