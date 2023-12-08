package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.JwtAuthenticationResponseDTO;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.services.AuthenticationService;
import com.school.bookstore.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer API", description = "Endpoints for managing customers")
@Validated
@RequestMapping("/api/customers")
@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<JwtAuthenticationResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authenticationService.signup(userDTO));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(userService.getUserById(customerId));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{customerId}")
    public HttpStatus deleteCustomer(@PathVariable Long customerId) {
        userService.deleteUser(customerId);
        return HttpStatus.NO_CONTENT;
    }
}
