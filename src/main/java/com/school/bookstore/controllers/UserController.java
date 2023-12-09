package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.JwtAuthenticationResponseDTO;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.services.AuthenticationService;
import com.school.bookstore.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured("ROLE_USER")
    @GetMapping("/{customerId}")
    public ResponseEntity<UserDTO> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(userService.getUserById(customerId));
    }

    @Secured("ROLE_STAFF")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Secured({ "ROLE_USER", "ROLE_STAFF" })
    @DeleteMapping("/{customerId}")
    public HttpStatus deleteCustomer(@PathVariable Long customerId) {
        userService.deleteUser(customerId);
        return HttpStatus.NO_CONTENT;
    }
}
