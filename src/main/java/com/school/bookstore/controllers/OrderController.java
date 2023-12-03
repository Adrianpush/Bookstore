package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/customers/{customerId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long customerId, @Valid @RequestBody OrderDTO shoppingCart) {
        return ResponseEntity.ok(orderService.createOrder(customerId, shoppingCart));
    }
}
