package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/{customerId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long customerId, @Valid @RequestBody OrderDTO shoppingCart) {
        return ResponseEntity.ok(orderService.createOrder(customerId, shoppingCart));
    }

    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getALlOrders());
    }

    @GetMapping("/{customerId}")
    private ResponseEntity<List<OrderDTO>> getAllOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getAllOrdersByCustomer(customerId));
    }
}
