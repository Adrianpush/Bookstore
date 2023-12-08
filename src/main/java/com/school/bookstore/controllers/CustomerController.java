package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer API", description = "Endpoints for managing customers")
@Validated
@RequestMapping("/api/customers")
@RestController
public class CustomerController {

    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createCustomer(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createCustomer(userDTO));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<UserDTO> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(userService.getCustomerById(customerId));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @DeleteMapping("/{customerId}")
    public HttpStatus deleteCustomer(@PathVariable Long customerId) {
        userService.deleteCustomer(customerId);
        return HttpStatus.NO_CONTENT;
    }
}
