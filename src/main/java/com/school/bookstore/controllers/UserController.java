package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.services.interfaces.JwtService;
import com.school.bookstore.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer API", description = "Endpoints for managing customers")
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @Secured({"ROLE_USER", "ROLE_STAFF"})
    @GetMapping("/{customerId}")
    public ResponseEntity<UserDTO> getCustomerById(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable(required = false) Long customerId) {
        return ResponseEntity.ok(userService.getUserById(jwtService.extractUserName(authorizationHeader.substring(7)), customerId));
    }

    @Secured("ROLE_STAFF")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Secured({ "ROLE_USER", "ROLE_STAFF" })
    @DeleteMapping("/{customerId}")
    public HttpStatus deleteCustomer(@RequestHeader(name = "Authorization") String authorizationHeader,
                                     @PathVariable Long customerId) {
        userService.deleteUser(jwtService.extractUserName(authorizationHeader.substring(7)),
                customerId);
        return HttpStatus.NO_CONTENT;
    }
}
