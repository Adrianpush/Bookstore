package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Secured("ROLE_USER")
    @PostMapping("customers/{customerId}")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long customerId,
            @Valid @RequestBody OrderDTO shoppingCart) {
        return ResponseEntity.ok(orderService.createOrder(customerId, shoppingCart));
    }

    @Secured("ROLE_STAFF")
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getALlOrders());
    }

    @Secured({ "ROLE_USER", "ROLE_STAFF" })
    @GetMapping("customers/{customerId}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getAllOrdersByCustomer(customerId));
    }

    @Secured({ "ROLE_USER", "ROLE_STAFF" })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
